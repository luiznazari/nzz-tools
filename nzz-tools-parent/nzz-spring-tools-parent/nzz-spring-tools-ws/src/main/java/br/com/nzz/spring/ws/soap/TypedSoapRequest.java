package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.WebServiceMessageSender;

import java.util.function.Consumer;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.spring.adapter.AdapterLoader;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.WebServiceMessageSenderBuilder;


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
public interface TypedSoapRequest<P, R> extends GenericSoapRequest<P, R> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	TypedSoapRequest<P, R> withAction(String soapAction);

	/**
	 * {@inheritDoc}
	 */
	@Override
	TypedSoapRequest<P, R> withEnvironment(Environment environment);

	/**
	 * {@inheritDoc}
	 */
	@Override
	TypedSoapRequest<P, R> withMessageSender(WebServiceMessageSender webServiceMessageSender);

	/**
	 * {@inheritDoc}
	 */
	@Override
	TypedSoapRequest<P, R> withMessageSender(Consumer<WebServiceMessageSenderBuilder> webServiceMessageSenderBuilderConsumer);

	/**
	 * Sets the response extractor function to parse the response.
	 *
	 * @param responseExtractorFunction the response extractor function
	 * @return the SOAP request
	 */
	TypedSoapRequest<P, R> withResponseExtractor(WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction);

	/**
	 * {@inheritDoc}
	 */
	@Override
	TypedSoapRequest<P, R> doWithWebServiceGateway(Consumer<WebServiceGatewaySupport> wsGatewayConsumer);

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

	static Jaxb2Marshaller createMarshaller(Class<?> payloadObjectClass) {
		return createMarshaller(payloadObjectClass.getPackage().getName());
	}

	static Jaxb2Marshaller createMarshaller(String contextPath) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(contextPath);
		marshaller.setAdapters(AdapterLoader.loadFromClassPath());
		return marshaller;
	}

	static <E, S> TypedSoapRequest<E, S> from(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		return new MarshallingSoapRequest<>(soapWebService, marshaller);
	}

	static <E, S> TypedSoapRequest<E, S> from(String url, Jaxb2Marshaller marshaller) {
		return new MarshallingSoapRequest<>(url, marshaller);
	}

}
