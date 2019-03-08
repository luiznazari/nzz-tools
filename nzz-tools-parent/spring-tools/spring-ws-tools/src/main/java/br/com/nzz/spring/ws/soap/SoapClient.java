package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.adapter.AdapterLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


/**
 * Interface para representar um cliente SOAP.
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
