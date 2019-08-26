package br.com.nzz.spring.soap;

import org.springframework.http.HttpStatus;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.function.Supplier;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.commons.concurrent.NzzFutures;
import br.com.nzz.spring.Environment;
import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;
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
 * @see SoapEnvelopeFaultMessageResolver
 */
@Log4j2
abstract class AbstractSoapRequest<P, R> implements SoapRequest<P, R> {

	private static final Integer DEFAULT_TIMEOUT_SECONDS = 60;

	private String soapAction;
	Environment environment = Environment.DEVELOPMENT;
	final SoapWebService webService;
	@Getter
	final SoapEnvelopeGatewaySupport wsGateway;

	AbstractSoapRequest(String url) {
		this(SoapWebService.from(url));
	}

	AbstractSoapRequest(SoapWebService soapWebService) {
		this.webService = soapWebService;
		this.wsGateway = new SoapEnvelopeGatewaySupport();
	}

	@Override
	public AbstractSoapRequest<P, R> withAction(String soapAction) {
		this.soapAction = soapAction;
		return this;
	}

	@Override
	public AbstractSoapRequest<P, R> withEnvironment(Environment environment) {
		this.environment = environment;
		return this;
	}

	@Override
	public AbstractSoapRequest<P, R> withRequestTimeoutInSeconds(Integer requestTimeoutInSeconds) {
		this.wsGateway.setRequestTimeoutInSeconds(requestTimeoutInSeconds);
		return this;
	}

	@Override
	public AbstractSoapRequest<P, R> withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.wsGateway.setConnectionTimeoutInSeconds(connectionTimeoutInSeconds);
		return this;
	}

	@Override
	public NzzCompletableFuture<R> send(P payload) {
		return NzzFutures.resolve(() -> this.sendSync(payload));
	}

	protected R send(Supplier<R> soapSenderSupplier) {
		try {
			log.trace(() -> "Preparing SOAP request...");
			return soapSenderSupplier.get();

		} catch (WebServiceIOException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				String timeoutMessage = String.format("The SOAP request had timed out. Timeout: %d seconds",
					Optional.ofNullable(this.wsGateway.getRequestTimeoutInSeconds()).orElse(DEFAULT_TIMEOUT_SECONDS));
				throw new WebServiceException(NzzConstants.INTEGRATION_ERROR, HttpStatus.REQUEST_TIMEOUT, timeoutMessage);

			} else {
				throw new WebServiceException(e, NzzConstants.INTEGRATION_ERROR, HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
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
