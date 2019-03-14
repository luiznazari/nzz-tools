package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.exception.WebServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * SOAP WebService Client. Responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (action). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * original error details.
 *
 * @param <P>
 * @param <R>
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
