package br.com.nzz.spring.ws.soap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ws.WebServiceMessage;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Supplier;

final class SoapRequestLogger {

	private static final Logger log = LogManager.getLogger(MarshallingSoapRequest.class);

	static void logSoapMessage(String message, WebServiceMessage soapMessage) {
		logSoapMessage(message, () -> soapMessageAsString(soapMessage));
	}

	static void logSoapMessageSource(String message, Source soapMessageSource) {
		logSoapMessage(message, () -> soapSourceAsString(soapMessageSource));
	}

	private static void logSoapMessage(String message, Supplier<String> soapXmlSupplier) {
		log.trace(() -> String.format("%s. Payload:\n%s", message, soapXmlSupplier.get()));
	}

	private static String soapMessageAsString(WebServiceMessage soapMessage) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapMessage.writeTo(out);
			return new String(out.toByteArray());
		} catch (IOException e) {
			log.trace("Error while parsing SOAP message as string.", e);
			return StringUtils.EMPTY;
		}
	}

	private static String soapSourceAsString(Source soapMessageSource) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			transformer.transform(soapMessageSource, result);
			return result.getWriter().toString();
		} catch (TransformerException e) {
			log.trace("Error while parsing SOAP message as string.", e);
			return StringUtils.EMPTY;
		}
	}

}
