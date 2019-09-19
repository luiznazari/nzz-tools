package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.nzz.spring.adapter.AdapterLoader;


/**
 * Interface to represent a SOAP Client.
 *
 * @author Luiz.Nazari
 */
public interface SoapClient {

	static Jaxb2Marshaller createMarshaller(Class<?> payloadObjectClass) {
		return createMarshaller(payloadObjectClass.getPackage().getName());
	}

	static Jaxb2Marshaller createMarshaller(String contextPath) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(contextPath);
		marshaller.setAdapters(AdapterLoader.loadFromClassPath());
		return marshaller;
	}

}
