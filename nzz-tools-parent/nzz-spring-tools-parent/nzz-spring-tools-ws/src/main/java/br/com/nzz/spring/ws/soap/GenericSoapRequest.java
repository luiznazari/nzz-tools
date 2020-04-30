package br.com.nzz.spring.ws.soap;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.WebServiceMessageSender;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.WebServiceMessageSenderBuilder;

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
public interface GenericSoapRequest<P, R> {

	/**
	 * Sets the SOAP action or SOAP method.
	 *
	 * @param soapAction the SOAP action
	 * @return the SOAP request
	 */
	GenericSoapRequest<P, R> withAction(String soapAction);

	/**
	 * Sets the environment of the request. Depending on the environment, a
	 * different WebService may be used as defined in the {@link SoapWebService}'s url.
	 *
	 * @param environment the request environment
	 * @return the SOAP request
	 */
	GenericSoapRequest<P, R> withEnvironment(Environment environment);

	/**
	 * Changes the message sender used by the WebService Gateway.
	 *
	 * @param webServiceMessageSender the message sender
	 * @return the SOAP request
	 */
	GenericSoapRequest<P, R> withMessageSender(WebServiceMessageSender webServiceMessageSender);

	/**
	 * Changes the message sender used by the WebService Gateway. The new message sender will be built
	 * with the {@link WebServiceMessageSenderBuilder} parameters configured in the consumer.
	 *
	 * @param webServiceMessageSenderBuilderConsumer the message sender builder consumer
	 * @return the SOAP request
	 */
	GenericSoapRequest<P, R> withMessageSender(Consumer<WebServiceMessageSenderBuilder> webServiceMessageSenderBuilderConsumer);

	/**
	 * Returns the current web service, with URLs and environment.
	 *
	 * @return the web service
	 */
	SoapWebService getWebService();

	/**
	 * <p>Returns the WebServiceGatewaySupport used by the request sender.
	 * <p>Allow behaviour modifications such as mocking.
	 *
	 * @return the gateway support
	 */
	WebServiceGatewaySupport getWebServiceGateway();

	/**
	 * <p>Register an consumer to the WebServiceGatewaySupport used by the request sender.
	 * <p>Allow behaviour modifications such as mocking.
	 *
	 * @param wsGatewayConsumer the {@link WebServiceGatewaySupport} consumer.
	 * @return the SOAP request
	 */
	GenericSoapRequest<P, R> doWithWebServiceGateway(Consumer<WebServiceGatewaySupport> wsGatewayConsumer);

	/**
	 * <p>Send an asynchronous request to the WebService with the specified payload object.</p>
	 * <p>No runtime exception will be thrown by this method, if any exception occurs while
	 * sending or receiving messages, it'll be returned in the {@link CompletableFuture}'s
	 * callbacks.</p>
	 *
	 * @param payload an completable future with the successful or unsuccessful response.
	 * @return the completable future that will be completed when the SOAP request is done
	 */
	CompletableFuture<R> send(@Nonnull P payload);

	/**
	 * <p>Send a synchronous request to the WebService with the specified payload object.</p>
	 *
	 * @param payload the request payload object
	 * @return the response object
	 * @throws WebServiceException         if an exception occurs while sending or receiving messages.
	 * @throws WebServiceInternalException if an internal exception occurs while sending or receiving messages.
	 */
	R sendSync(P payload) throws WebServiceInternalException;

}
