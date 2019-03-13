package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.NzzWsConstants;
import br.com.nzz.spring.ws.exception.WebServiceException;
import br.com.nzz.spring.ws.exception.WebServiceInternalException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.support.MarshallingUtils;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import javax.xml.bind.JAXBElement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Cliente WebService que utiliza SOAP. É responsável pela configuração de envio
 * e tratamento da resposta de um método (ação) SOAP. Em caso de falha em algum
 * fluxo do SOAP, uma {@link WebServiceException} será lançada contendo os dados
 * da falha.
 *
 * @author Luiz Felipe Nazari
 * @see EnvelopeSoapFaultMessageResolver
 */
@Log4j2
public class SoapRequest<P, R> {

	private static final Integer DEFAULT_TIMEOUT_SECONDS = 60;
	private static final String MENSAGEM_ERRO_DESCONHECIDO = "O WebService requisitado retornou uma mensagem SOAP inválida.";

	/*
	 * EnvelopeSoap é utilizado como atributo final de Beans, portanto, se
	 * comporta como tal e é mantido como uma instância para cada Bean. Dessa
	 * forma, os atributos da classe devem ser finais ou efetivamente finais
	 * (uma vez atribuído valor, nunca mais é alterado).
	 */

	private String soapAction;
	private WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction;

	private Environment environment;
	private final SoapWebService webService;
	private final Jaxb2Marshaller marshaller;
	@Getter
	private final EnvelopeSoapGatewaySupport wsGateway;

	public SoapRequest(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
		this.webService = soapWebService;
		this.wsGateway = new EnvelopeSoapGatewaySupport(marshaller);
	}

	public SoapRequest(String url, Jaxb2Marshaller marshaller) {
		this(SoapWebService.from(url), marshaller);
	}

	public final R send(P payload) {
		try {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);
			log.trace(() -> "Preparing SOAP request...");
			return wsGateway.getWebServiceTemplate().sendAndReceive(wsdlUrl,
				configureRequest(payload), defaultResponseExtractor());

		} catch (WebServiceIOException e) {
			if (e.getCause() != null && e.getCause() instanceof SocketTimeoutException) {
				String timeoutMessage = String.format("The SOAP request had timed out. Timeout: %d seconds",
					Optional.ofNullable(this.wsGateway.requestTimeoutInSeconds).orElse(DEFAULT_TIMEOUT_SECONDS));
				throw new WebServiceException(NzzWsConstants.SOAP_INTEGRATION_ERROR, HttpStatus.REQUEST_TIMEOUT, timeoutMessage);

			} else {
				throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR, HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
			}
		}
	}

	public SoapRequest<P, R> withAction(String soapAction) {
		this.soapAction = soapAction;
		return this;
	}

	public SoapRequest<P, R> withEnvironment(Environment environment) {
		this.environment = environment;
		return this;
	}

	public SoapRequest<P, R> withResponseExtractor(WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction) {
		this.responseExtractorFunction = responseExtractorFunction;
		return this;
	}

	public SoapRequest<P, R> withRequestTimeoutInSeconds(Integer requestTimeoutInSeconds) {
		this.wsGateway.requestTimeoutInSeconds = requestTimeoutInSeconds;
		return this;
	}

	public SoapRequest<P, R> withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.wsGateway.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
		return this;
	}


	private WebServiceMessageCallback configureRequest(P payload) {
		return message -> {
			MarshallingUtils.marshal(marshaller, payload, message);

			if (this.soapAction != null) {
				new SoapActionCallback(this.soapAction).doWithMessage(message);
			}

			SoapRequest.logSoapMessage("Sending SOAP request", message);
		};
	}

	@SuppressWarnings("unchecked")
	private WebServiceMessageExtractor<R> defaultResponseExtractor() {
		return message -> {
			try {
				SoapRequest.logSoapMessage("Received SOAP response", message);

				// Remove vínculos com classe resultante.
				marshaller.setMappedClass(null);
				R response;

				Object responsePayload = MarshallingUtils.unmarshal(marshaller, message);
				if (responsePayload instanceof JAXBElement) {
					response = ((JAXBElement<R>) responsePayload).getValue();
				} else {
					response = (R) responsePayload;
				}

				return responseExtractorFunction == null ? response : responseExtractorFunction.apply(marshaller, response);
			} catch (WebServiceInternalException e) {
				throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR, NzzWsConstants.INTERNAL_ERROR, e.getMessage());
			}
		};
	}

	/**
	 * GatewaySupport gerenciado por um cliente WebService, ao qual são
	 * delegadas as ações e conexões.
	 *
	 * @author Luiz Felipe Nazari
	 */
	private static final class EnvelopeSoapGatewaySupport extends WebServiceGatewaySupport {

		private Integer requestTimeoutInSeconds;
		private Integer connectionTimeoutInSeconds;

		EnvelopeSoapGatewaySupport(Jaxb2Marshaller marshaller) {
			this.setMarshaller(marshaller);
			this.setUnmarshaller(marshaller);
			this.configureTimeout();
			this.getWebServiceTemplate().setFaultMessageResolver(new EnvelopeSoapFaultMessageResolver());
		}

		private void configureTimeout() {
			WebServiceMessageSender[] webServiceMessageSenders = this.getWebServiceTemplate().getMessageSenders();
			if (webServiceMessageSenders != null) {
				for (WebServiceMessageSender sender : webServiceMessageSenders) {
					this.configureSenderTimeout(sender);
				}
			}
		}

		private void configureSenderTimeout(WebServiceMessageSender sender) {
			if (sender instanceof HttpUrlConnectionMessageSender) {
				HttpUrlConnectionMessageSender httpSender = (HttpUrlConnectionMessageSender) sender;
				if (this.requestTimeoutInSeconds != null) {
					httpSender.setReadTimeout(Duration.ofSeconds(requestTimeoutInSeconds));
				}
				if (connectionTimeoutInSeconds != null) {
					httpSender.setConnectionTimeout(Duration.ofSeconds(connectionTimeoutInSeconds));
				}

			} else if (sender instanceof HttpComponentsMessageSender) {
				HttpComponentsMessageSender httpSender = (HttpComponentsMessageSender) sender;
				if (requestTimeoutInSeconds != null) {
					httpSender.setReadTimeout((int) TimeUnit.SECONDS.toMillis(requestTimeoutInSeconds));
				}
				if (connectionTimeoutInSeconds != null) {
					httpSender.setConnectionTimeout((int) TimeUnit.SECONDS.toMillis(connectionTimeoutInSeconds));
				}

			} else if (this.requestTimeoutInSeconds != null || this.connectionTimeoutInSeconds != null) {
				log.warn(() -> "Could not set timeout for sender " + sender.getClass().getName());
			}
		}

	}

	/**
	 * Tratamento de mensagems de falha retornados na requisição SOAP. Sempre
	 * lançará uma {@link WebServiceException} contendo o código e a descrição
	 * do erro retornado do WebService.
	 *
	 * @author Luiz Felipe Nazari
	 */
	private static final class EnvelopeSoapFaultMessageResolver implements FaultMessageResolver {

		@Override
		public void resolveFault(WebServiceMessage message) {
			try {
				SoapRequest.logSoapMessage("Received SOAP fault response", message);

				SoapMessage soapMessage = (SoapMessage) message;
				SoapFault fault = soapMessage.getSoapBody().getFault();
				String code = fault.getFaultCode().getLocalPart();
				String failMessage = fault.getFaultStringOrReason();

				throw new WebServiceException(NzzWsConstants.SOAP_INTEGRATION_ERROR, code, failMessage);

			} catch (NullPointerException | ClassCastException e) {
				/*
				 * Apenas por segurança: estes erros não foram ocasionados em
				 * nenhuma situação. Entretanto, não podemos gerar erros no
				 * tratamento de mensagens de falha do SOAP.
				 */
				throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR, NzzWsConstants.INTERNAL_ERROR,
					MENSAGEM_ERRO_DESCONHECIDO);
			}
		}

	}

	private static void logSoapMessage(String message, WebServiceMessage soapMessage) {
		log.trace(() -> String.format("%s. Payload:\n%s", message, soapMessageAsString(soapMessage)));
	}

	private static String soapMessageAsString(WebServiceMessage soapMessage) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapMessage.writeTo(out);
			return new String(out.toByteArray());
		} catch (IOException e) {
			log.trace("Error while parsing WebServiceMessage as string.", e);
			return StringUtils.EMPTY;
		}
	}


}
