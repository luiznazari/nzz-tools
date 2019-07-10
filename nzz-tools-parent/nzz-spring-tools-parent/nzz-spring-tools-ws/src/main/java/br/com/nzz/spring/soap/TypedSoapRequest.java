package br.com.nzz.spring.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.nzz.spring.exception.WebServiceException;
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
public class TypedSoapRequest<P, R> extends MarshallingSoapRequest<P, R> {

	public TypedSoapRequest(String url, Jaxb2Marshaller marshaller) {
		super(url, marshaller);
	}

	public TypedSoapRequest(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		super(soapWebService, marshaller);
	}

}
