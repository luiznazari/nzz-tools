package br.com.nzz.spring.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.nzz.spring.adapter.AdapterLoader;


/**
 * Interface to represent an SOAP Client.
 *
 * @author Luiz.Nazari
 */
public interface SoapClient {

	default <O> Jaxb2Marshaller marshaller(Class<O> payloadObjectClass) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(payloadObjectClass.getPackage().getName());

		marshaller.setAdapters(AdapterLoader.loadFromClassPath());

		return marshaller;
	}

}
