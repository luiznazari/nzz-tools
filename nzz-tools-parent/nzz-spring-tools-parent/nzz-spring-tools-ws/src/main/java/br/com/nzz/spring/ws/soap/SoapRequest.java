package br.com.nzz.spring.ws.soap;

import org.springframework.ws.transport.WebServiceMessageSender;

import java.util.function.Consumer;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.WebService;
import br.com.nzz.spring.ws.WebServiceMessageSenderBuilder;

/**
 * SOAP WebService Request, responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (SOAPAction). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * the original error details.
 *
 * @author Luiz Felipe Nazari
 */
public interface SoapRequest extends GenericSoapRequest<String, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	SoapRequest withAction(String soapAction);

	/**
	 * {@inheritDoc}
	 */
	@Override
	SoapRequest withEnvironment(Environment environment);

	/**
	 * {@inheritDoc}
	 */
	@Override
	SoapRequest withMessageSender(WebServiceMessageSender webServiceMessageSender);

	/**
	 * {@inheritDoc}
	 */
	@Override
	SoapRequest withMessageSender(Consumer<WebServiceMessageSenderBuilder> webServiceMessageSenderBuilderConsumer);

	/**
	 * {@inheritDoc}
	 */
	@Override
	NzzCompletableFuture<String> send(String payload);

	/**
	 * {@inheritDoc}
	 */
	@Override
	String sendSync(String payload) throws WebServiceException, WebServiceInternalException;

	static SoapRequest from(SoapWebService soapWebService) {
		return new SimpleSoapRequest(soapWebService);
	}

	static SoapRequest from(String url) {
		return new SimpleSoapRequest(url);
	}

}
