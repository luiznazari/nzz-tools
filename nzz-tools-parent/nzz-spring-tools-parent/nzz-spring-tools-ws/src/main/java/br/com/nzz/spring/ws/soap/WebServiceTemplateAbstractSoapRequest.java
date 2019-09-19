package br.com.nzz.spring.ws.soap;

import org.springframework.http.HttpStatus;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.transport.WebServiceMessageSender;

import java.net.SocketTimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.commons.concurrent.NzzFutures;
import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.HttpClientWebServiceMessageSenderBuilder;
import br.com.nzz.spring.ws.WebServiceMessageSenderBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * SOAP WebService Client. Responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (action). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * original error details.
 *
 * @param <P> type of the payload object
 * @param <R> type of the response object
 * @author Luiz Felipe Nazari
 * @see SoapRequestFaultMessageResolver
 */
@Log4j2
public abstract class WebServiceTemplateAbstractSoapRequest<P, R> implements GenericSoapRequest<P, R> {

	private String soapAction;
	Environment environment = Environment.DEVELOPMENT;
	final SoapWebService webService;
	@Getter
	final SoapRequestGatewaySupport wsGateway;

	WebServiceTemplateAbstractSoapRequest(String url) {
		this(SoapWebService.from(url));
	}

	WebServiceTemplateAbstractSoapRequest(SoapWebService soapWebService) {
		this.webService = soapWebService;
		this.wsGateway = new SoapRequestGatewaySupport();
	}

	@Override
	public WebServiceTemplateAbstractSoapRequest<P, R> withAction(String soapAction) {
		this.soapAction = soapAction;
		return this;
	}

	@Override
	public WebServiceTemplateAbstractSoapRequest<P, R> withEnvironment(Environment environment) {
		this.environment = environment;
		return this;
	}

	@Override
	public WebServiceTemplateAbstractSoapRequest<P, R> withMessageSender(Consumer<WebServiceMessageSenderBuilder> webServiceMessageSenderBuilderConsumer) {
		WebServiceMessageSenderBuilder webServiceMessageSenderBuilder = new HttpClientWebServiceMessageSenderBuilder();
		webServiceMessageSenderBuilderConsumer.accept(webServiceMessageSenderBuilder);
		return this.withMessageSender(webServiceMessageSenderBuilder.build());
	}

	@Override
	public WebServiceTemplateAbstractSoapRequest<P, R> withMessageSender(WebServiceMessageSender webServiceMessageSender) {
		this.wsGateway.changeMessageSender(webServiceMessageSender);
		return this;
	}

	@Override
	public NzzCompletableFuture<R> send(P payload) {
		return NzzFutures.resolve(() -> this.sendSync(payload));
	}

	@Override
	public SoapWebService getWebService() {
		return this.webService;
	}

	protected R send(Supplier<R> soapSenderSupplier) {
		try {
			log.trace(() -> "Preparing SOAP request...");
			return soapSenderSupplier.get();

		} catch (WebServiceIOException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new WebServiceException(NzzConstants.INTEGRATION_ERROR)
					.parameters(HttpStatus.REQUEST_TIMEOUT, "The SOAP request had timed out.");

			} else {
				throw new WebServiceException(NzzConstants.INTEGRATION_ERROR, e)
					.parameters(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
			}
		}
	}

	WebServiceMessageCallback configureRequestCallback() {
		return message -> {
			if (this.soapAction != null) {
				new SoapActionCallback(this.soapAction).doWithMessage(message);
			}

			SoapRequestLogger.logSoapMessage("Sending SOAP request", message);
		};
	}

}
