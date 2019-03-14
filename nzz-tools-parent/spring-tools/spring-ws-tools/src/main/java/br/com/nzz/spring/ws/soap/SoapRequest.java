package br.com.nzz.spring.ws.soap;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.commons.concurrent.NzzFutures;
import br.com.nzz.spring.ws.exception.WebServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

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
public abstract class SoapRequest extends MarshallingSoapRequest<Object, Object> {

	public SoapRequest(String url, Jaxb2Marshaller marshaller) {
		super(url, marshaller);
	}

	public SoapRequest(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		super(soapWebService, marshaller);
	}

	public <R> NzzCompletableFuture<R> send(Object payload, Class<R> responseClass) {
		return NzzFutures.resolve(() -> this.sendSync(payload, responseClass));
	}

	public <R> NzzCompletableFuture<R> sendXml(String payload, Class<R> responseClass) {
		return NzzFutures.resolve(() -> this.sendXmlSync(payload, responseClass));
	}

	public <R> R sendSync(Object payload, Class<R> responseClass) {
		return responseClass.cast(super.sendSync(payload));
	}

	public <R> R sendXmlSync(String payload, Class<R> responseClass) {
		return responseClass.cast(super.sendXmlSync(payload));
	}

}
