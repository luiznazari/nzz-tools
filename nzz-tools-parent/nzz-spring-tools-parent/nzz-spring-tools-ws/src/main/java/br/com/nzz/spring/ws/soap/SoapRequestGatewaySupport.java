package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.WebServiceMessageSender;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * GatewaySupport managed by a WebService client to delegate SOAP action and connections.
 *
 * @author Luiz Felipe Nazari
 */
@Log4j2
@Getter(AccessLevel.PACKAGE)
final class SoapRequestGatewaySupport extends WebServiceGatewaySupport {

	SoapRequestGatewaySupport(Jaxb2Marshaller marshaller) {
		this();
		this.setMarshaller(marshaller);
		this.setUnmarshaller(marshaller);
	}

	SoapRequestGatewaySupport() {
		this.getWebServiceTemplate().setFaultMessageResolver(new SoapRequestFaultMessageResolver());
	}

	void changeMessageSender(WebServiceMessageSender webServiceMessageSender) {
		this.getWebServiceTemplate().setMessageSender(webServiceMessageSender);
	}

}