package br.com.nzz.spring.soap;

import org.springframework.ws.client.core.SourceExtractor;

import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import lombok.extern.log4j.Log4j2;

/**
 * @author Luiz.Nazari
 */
@Log4j2
final class SourceStringResponseExtractor implements SourceExtractor<String> {

	@Override
	public String extractData(Source source) throws TransformerException {
		SoapRequestLogger.logSoapMessageSource("Received SOAP response", source);
		return soapSourceAsString(source);
	}

	private static String soapSourceAsString(Source soapMessageSource) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		transformer.transform(soapMessageSource, result);
		return result.getWriter().toString();
	}

}
