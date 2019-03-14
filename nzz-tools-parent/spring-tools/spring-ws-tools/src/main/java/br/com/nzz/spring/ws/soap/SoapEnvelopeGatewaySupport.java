package br.com.nzz.spring.ws.soap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * GatewaySupport managed by a WebService client to delegate SOAP action and connections.
 *
 * @author Luiz Felipe Nazari
 */
@Log4j2
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
final class SoapEnvelopeGatewaySupport extends WebServiceGatewaySupport {

	private Integer requestTimeoutInSeconds;
	private Integer connectionTimeoutInSeconds;

	SoapEnvelopeGatewaySupport(Jaxb2Marshaller marshaller) {
		this.setMarshaller(marshaller);
		this.setUnmarshaller(marshaller);
		this.configureTimeout();
		this.getWebServiceTemplate().setFaultMessageResolver(new SoapEnvelopeFaultMessageResolver());
	}

	private void configureTimeout() {
		WebServiceMessageSender[] webServiceMessageSenders = this.getWebServiceTemplate().getMessageSenders();
		if (webServiceMessageSenders != null) {
			for (WebServiceMessageSender sender : webServiceMessageSenders) {
				this.configureSenderTimeout(sender);
			}
		}
	}

	private void configureSenderTimeout(WebServiceMessageSender sender) {
		if (sender instanceof HttpUrlConnectionMessageSender) {
			HttpUrlConnectionMessageSender httpSender = (HttpUrlConnectionMessageSender) sender;
			if (this.requestTimeoutInSeconds != null) {
				httpSender.setReadTimeout(Duration.ofSeconds(requestTimeoutInSeconds));
			}
			if (connectionTimeoutInSeconds != null) {
				httpSender.setConnectionTimeout(Duration.ofSeconds(connectionTimeoutInSeconds));
			}

		} else if (sender instanceof HttpComponentsMessageSender) {
			HttpComponentsMessageSender httpSender = (HttpComponentsMessageSender) sender;
			if (requestTimeoutInSeconds != null) {
				httpSender.setReadTimeout((int) TimeUnit.SECONDS.toMillis(requestTimeoutInSeconds));
			}
			if (connectionTimeoutInSeconds != null) {
				httpSender.setConnectionTimeout((int) TimeUnit.SECONDS.toMillis(connectionTimeoutInSeconds));
			}

		} else if (this.requestTimeoutInSeconds != null || this.connectionTimeoutInSeconds != null) {
			log.warn(() -> "Could not set timeout for sender " + sender.getClass().getName());
		}
	}

}