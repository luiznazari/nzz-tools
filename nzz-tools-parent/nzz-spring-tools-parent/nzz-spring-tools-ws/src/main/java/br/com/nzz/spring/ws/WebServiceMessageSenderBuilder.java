package br.com.nzz.spring.ws;

import org.springframework.ws.transport.WebServiceMessageSender;

import java.util.function.UnaryOperator;

import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.SSLProtocolVersion;

public interface WebServiceMessageSenderBuilder {

	/**
	 * <p>Sets the limit timeout in seconds to wait reading the WebService response.
	 * <p>The timeout will be caused if a connection is established but there is no
	 * response within the time limit.
	 *
	 * @param readTimeoutInSeconds the read timeout in seconds
	 * @return the builder
	 */
	WebServiceMessageSenderBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds);

	/**
	 * <p>Sets the limit connection in seconds while trying to connect to the WebService.
	 * <p>The timeout will be caused if no connection is established within the time limit.
	 *
	 * @param connectionTimeoutInSeconds the connection timeout in seconds
	 * @return the builder
	 */
	WebServiceMessageSenderBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds);

	WebServiceMessageSenderBuilder withPasswordDecoder(UnaryOperator<String> passwordDecoderFunction);

	WebServiceMessageSenderBuilder withKeyStore(KeyStoreResource keyStore);

	WebServiceMessageSenderBuilder withTrustStore(KeyStoreResource trustStore);

	WebServiceMessageSenderBuilder withSSLProtocolVersion(SSLProtocolVersion sslProtocolVersion);

	WebServiceMessageSender build();

}
