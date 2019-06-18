package br.com.nzz.spring.ws.soap;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.commons.concurrent.NzzFutures;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.NzzWsConstants;
import br.com.nzz.spring.ws.exception.WebServiceException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.support.MarshallingUtils;
import org.springframework.xml.transform.StringSource;

import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * SOAP WebService Client. Responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (action). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * original error details.
 *
 * @author Luiz Felipe Nazari
 * @see SoapEnvelopeFaultMessageResolver
 */
@Log4j2
public abstract class MarshallingSoapRequest<P, R> {

	private static final Integer DEFAULT_TIMEOUT_SECONDS = 60;

	private String soapAction;
	private WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction;

	private Environment environment;
	private final SoapWebService webService;
	private final Jaxb2Marshaller marshaller;
	@Getter
	private final SoapEnvelopeGatewaySupport wsGateway;

	public MarshallingSoapRequest(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
		this.webService = soapWebService;
		this.wsGateway = new SoapEnvelopeGatewaySupport(marshaller);
	}

	public MarshallingSoapRequest(String url, Jaxb2Marshaller marshaller) {
		this(SoapWebService.from(url), marshaller);
	}

	public MarshallingSoapRequest<P, R> withAction(String soapAction) {
		this.soapAction = soapAction;
		return this;
	}

	public MarshallingSoapRequest<P, R> withEnvironment(Environment environment) {
		this.environment = environment;
		return this;
	}

	public MarshallingSoapRequest<P, R> withResponseExtractor(WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction) {
		this.responseExtractorFunction = responseExtractorFunction;
		return this;
	}

	public MarshallingSoapRequest<P, R> withRequestTimeoutInSeconds(Integer requestTimeoutInSeconds) {
		this.wsGateway.setRequestTimeoutInSeconds(requestTimeoutInSeconds);
		return this;
	}

	public MarshallingSoapRequest<P, R> withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.wsGateway.setConnectionTimeoutInSeconds(connectionTimeoutInSeconds);
		return this;
	}

	public NzzCompletableFuture<R> send(P payload) {
		return NzzFutures.resolve(() -> this.sendSync(payload));
	}

	public NzzCompletableFuture<R> sendXml(String payload) {
		return NzzFutures.resolve(() -> this.sendXmlSync(payload));
	}

	public R sendSync(P payload) {
		return this.send(() -> {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);

			WebServiceMessageExtractor<R> messageResponseExtractor = new WebServiceMessageUnMarshallerResponseExtractor<>(
				this.marshaller, this.responseExtractorFunction);

			return this.wsGateway.getWebServiceTemplate().sendAndReceive(wsdlUrl,
				configureRequestCallback(payload),
				messageResponseExtractor);
		});
	}

	public R sendXmlSync(String payload) {
		return this.send(() -> {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);

			SourceExtractor<R> sourceResponseExtractor = new SourceUnMarshallerResponseExtractor<>(
				this.marshaller, this.responseExtractorFunction);

			return this.wsGateway.getWebServiceTemplate().sendSourceAndReceive(wsdlUrl,
				new StringSource(payload),
				configureRequestCallback(),
				sourceResponseExtractor);
		});
	}

	private R send(Supplier<R> soapSenderSupplier) {
		try {
			log.trace(() -> "Preparing SOAP request...");
			return soapSenderSupplier.get();

		} catch (WebServiceIOException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				String timeoutMessage = String.format("The SOAP request had timed out. Timeout: %d seconds",
					Optional.ofNullable(this.wsGateway.getRequestTimeoutInSeconds()).orElse(DEFAULT_TIMEOUT_SECONDS));
				throw new WebServiceException(NzzWsConstants.SOAP_INTEGRATION_ERROR, HttpStatus.REQUEST_TIMEOUT, timeoutMessage);

			} else {
				throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR, HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
			}
		}
	}

	private WebServiceMessageCallback configureRequestCallback(P payload) {
		return message -> {
			MarshallingUtils.marshal(marshaller, payload, message);
			configureRequestCallback().doWithMessage(message);
		};
	}

	private WebServiceMessageCallback configureRequestCallback() {
		return message -> {
			if (this.soapAction != null) {
				new SoapActionCallback(this.soapAction).doWithMessage(message);
			}

			SoapRequestLogger.logSoapMessage("Sending SOAP request", message);
		};
	}

}
