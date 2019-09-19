package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.support.MarshallingUtils;

import java.io.IOException;

final class WebServiceMessageUnMarshallerResponseExtractor<R> extends AbstractUnMarshallingResponseExtractor<R, WebServiceMessage> implements WebServiceMessageExtractor<R> {

	WebServiceMessageUnMarshallerResponseExtractor(Jaxb2Marshaller marshaller, WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction) {
		super(marshaller, responseExtractorFunction);
	}

	@Override
	public R extractData(WebServiceMessage message) throws IOException {
		SoapRequestLogger.logSoapMessage("Received SOAP response", message);
		return super.extractMessageData(message);
	}

	@Override
	protected Object unMarshall(WebServiceMessage message) throws IOException {
		return MarshallingUtils.unmarshal(marshaller, message);
	}

}
