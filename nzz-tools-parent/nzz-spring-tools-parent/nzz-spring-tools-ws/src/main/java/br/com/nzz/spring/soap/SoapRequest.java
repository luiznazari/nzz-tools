package br.com.nzz.spring.soap;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.spring.Environment;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;

/**
 * SOAP WebService Request, responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (SOAPAction). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * the original error details.
 *
 * @param <P> the type of the payload object
 * @param <R> the type of the response object
 * @author Luiz Felipe Nazari
 */
public interface SoapRequest<P, R> {

	/**
	 * Sets the SOAP action or SOAP method.
	 *
	 * @param soapAction the SOAP action
	 * @return the SOAP request
	 */
	SoapRequest<P, R> withAction(String soapAction);

	/**
	 * Sets the environment of the request. Depending on the environment, a
	 * different WebService may be used as defined in the {@link SoapWebService}'s url.
	 *
	 * @param environment the request environment
	 * @return the SOAP request
	 */
	SoapRequest<P, R> withEnvironment(Environment environment);

	/**
	 * Sets the limit timeout in seconds to wait for a response when requesting to the
	 * WebService.
	 *
	 * @param requestTimeoutInSeconds the request timeout in seconds
	 * @return the SOAP request
	 */
	SoapRequest<P, R> withRequestTimeoutInSeconds(Integer requestTimeoutInSeconds);

	/**
	 * Sets the limit connection in seconds while trying to connect to the WebService.
	 *
	 * @param connectionTimeoutInSeconds the connection timeout in seconds
	 * @return the SOAP request
	 */
	SoapRequest<P, R> withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds);

	/**
	 * <p>Send an asynchronous request to the WebService with the specified payload object.</p>
	 * <p>No runtime exception will be thrown by this method, if any exception occurs while
	 * sending or receiving messages, it'll be returned in the {@link NzzCompletableFuture}'s
	 * callbacks.</p>
	 *
	 * @param payload an completable future with the successful or unsuccessful response.
	 * @return the SOAP request
	 */
	NzzCompletableFuture<R> send(P payload);

	/**
	 * <p>Send a synchronous request to the WebService with the specified payload object.</p>
	 *
	 * @param payload the request payload object
	 * @return the response object
	 * @throws WebServiceException         if an exception occurs while sending or receiving messages.
	 * @throws WebServiceInternalException if an internal exception occurs while sending or receiving messages.
	 */
	R sendSync(P payload) throws WebServiceException, WebServiceInternalException;

	default SoapRequest<String, String> from(SoapWebService soapWebService) {
		return new SimpleSoapRequest(soapWebService);
	}

	default SoapRequest<String, String> from(String url) {
		return new SimpleSoapRequest(url);
	}

}
