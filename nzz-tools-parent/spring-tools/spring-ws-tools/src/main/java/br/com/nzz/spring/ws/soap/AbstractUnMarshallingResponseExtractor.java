package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.NzzWsConstants;
import br.com.nzz.spring.ws.exception.WebServiceException;
import br.com.nzz.spring.ws.exception.WebServiceInternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.JAXBElement;
import java.io.IOException;

@RequiredArgsConstructor
@SuppressWarnings("unchecked")
abstract class AbstractUnMarshallingResponseExtractor<R, D> {

	protected final Jaxb2Marshaller marshaller;
	protected final WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction;

	R extractMessageData(D messageData) throws IOException {
		try {
			// Clear marshaller's bindings with previous result class.
			marshaller.setMappedClass(null);
			R response;

			Object responsePayload = this.unMarshall(messageData);
			if (responsePayload instanceof JAXBElement) {
				response = ((JAXBElement<R>) responsePayload).getValue();
			} else {
				response = (R) responsePayload;
			}

			return responseExtractorFunction == null
				? response
				: responseExtractorFunction.apply(marshaller, response);
		} catch (WebServiceInternalException e) {
			throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR, NzzWsConstants.INTERNAL_ERROR, e.getMessage());
		}
	}

	protected abstract Object unMarshall(D messageData) throws IOException;

}
