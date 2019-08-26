package br.com.nzz.spring.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;


/**
 * Typed SOAP WebService Request, responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (SOAPAction). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with the
 * original error details.
 * <br>
 * The sent payload and the received response will be parsed to/from Java objects.
 *
 * @param <P> type of the payload object
 * @param <R> type of the response object
 * @author Luiz Felipe Nazari
 */
public interface TypedSoapRequest<P, R> extends SoapRequest<P, R> {

	/**
	 * <p>Send an asynchronous request to the WebService with the specified payload XML.</p>
	 * <p>No runtime exception will be thrown by this method, if any exception occurs while
	 * sending or receiving messages, it'll be returned in the {@link NzzCompletableFuture}'s
	 * callbacks.</p>
	 *
	 * @param payload an completable future with the successful or unsuccessful response.
	 * @return the SOAP request
	 */
	NzzCompletableFuture<R> sendXml(String payload);

	/**
	 * <p>Send a synchronous request to the WebService with the specified payload XML.</p>
	 *
	 * @param payload the request payload object
	 * @return the parsed response object
	 * @throws WebServiceException         if an exception occurs while sending or receiving messages.
	 * @throws WebServiceInternalException if an internal exception occurs while sending or receiving messages.
	 */
	R sendXmlSync(String payload) throws WebServiceException, WebServiceInternalException;

	static <E, S> TypedSoapRequest<E, S> from(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		return new MarshallingSoapRequest<>(soapWebService, marshaller);
	}

	static <E, S> TypedSoapRequest<E, S> from(String url, Jaxb2Marshaller marshaller) {
		return new MarshallingSoapRequest<>(url, marshaller);
	}

}
