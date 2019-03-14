package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.SourceExtractor;

import javax.xml.transform.Source;
import java.io.IOException;

class SourceUnMarshallerResponseExtractor<R> extends AbstractUnMarshallingResponseExtractor<R, Source> implements SourceExtractor<R> {

	SourceUnMarshallerResponseExtractor(Jaxb2Marshaller marshaller, WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction) {
		super(marshaller, responseExtractorFunction);
	}

	@Override
	public R extractData(Source source) throws IOException {
		SoapRequestLogger.logSoapMessageSource("Received SOAP response", source);
		return super.extractMessageData(source);
	}

	@Override
	protected Object unMarshall(Source source) {
		return marshaller.unmarshal(source);
	}

}
