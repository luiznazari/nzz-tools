package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.IOException;

import javax.xml.bind.JAXBElement;

import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("unchecked")
abstract class AbstractUnMarshallingResponseExtractor<R, D> {

	protected final Jaxb2Marshaller marshaller;
	protected final WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction;

	R extractMessageData(D messageData) throws IOException {
		try {
			// Clear createMarshaller's bindings with previous result class.
			// noinspection ConstantConditions
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
			throw new WebServiceException(NzzConstants.INTEGRATION_ERROR, e)
				.parameters(NzzConstants.INTERNAL_ERROR, e.getMessage());
		}
	}

	protected abstract Object unMarshall(D messageData) throws IOException;

}
