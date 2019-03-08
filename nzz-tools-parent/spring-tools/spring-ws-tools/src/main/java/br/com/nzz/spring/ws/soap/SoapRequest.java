package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.NzzWsConstants;
import br.com.nzz.spring.ws.exception.WebServiceException;
import br.com.nzz.spring.ws.exception.WebServiceInternalException;
import lombok.AccessLevel;
import lombok.Getter;
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

import javax.xml.bind.JAXBElement;

/**
 * Cliente WebService que utiliza SOAP. É responsável pela configuração de envio
 * e tratamento da resposta de um método (ação) SOAP. Em caso de falha em algum
 * fluxo do SOAP, uma {@link WebServiceException} será lançada contendo os dados
 * da falha.
 *
 * @author Luiz Felipe Nazari
 * @see EnvelopeSoapFaultMessageResolver
 */
final class SoapRequest<P, R> {

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
	@Getter(AccessLevel.PACKAGE)
	private final WebServiceGatewaySupport wsGateway;

	SoapRequest(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
		this.webService = soapWebService;
		this.wsGateway = new EnvelopeSoapGatewaySupport(marshaller);
	}

	final R send(P payload) {
		try {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);
			return wsGateway.getWebServiceTemplate().sendAndReceive(wsdlUrl,
				configureRequest(payload), defaultResponseExtractor());
		} catch (WebServiceIOException e) {
			throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR, HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
		}
	}

	SoapRequest<P, R> action(String soapAction) {
		this.soapAction = soapAction;
		return this;
	}

	SoapRequest<P, R> environment(Environment environment) {
		this.environment = environment;
		return this;
	}

	SoapRequest<P, R> responseExtractor(WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction) {
		this.responseExtractorFunction = responseExtractorFunction;
		return this;
	}

	private WebServiceMessageCallback configureRequest(P payload) {
		return message -> {
			MarshallingUtils.marshal(marshaller, payload, message);

			if (this.soapAction != null) {
				new SoapActionCallback(this.soapAction).doWithMessage(message);
			}
		};
	}

	@SuppressWarnings("unchecked")
	private WebServiceMessageExtractor<R> defaultResponseExtractor() {
		return message -> {
			try {
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

		EnvelopeSoapGatewaySupport(Jaxb2Marshaller marshaller) {
			this.setMarshaller(marshaller);
			this.setUnmarshaller(marshaller);
			this.getWebServiceTemplate().setFaultMessageResolver(new EnvelopeSoapFaultMessageResolver());
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

}
