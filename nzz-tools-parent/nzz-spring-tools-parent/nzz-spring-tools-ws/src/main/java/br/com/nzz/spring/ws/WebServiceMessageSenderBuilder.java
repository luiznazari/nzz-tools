package br.com.nzz.spring.ws;

import org.springframework.ws.transport.WebServiceMessageSender;

import java.util.function.Function;

import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.SSLProtocolVersion;

public interface WebServiceMessageSenderBuilder {

	/**
	 * Sets the limit timeout in seconds to wait reading the WebService response.
	 *
	 * @param readTimeoutInSeconds the read timeout in seconds
	 * @return the builder
	 */
	WebServiceMessageSenderBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds);

	/**
	 * Sets the limit connection in seconds while trying to connect to the WebService.
	 *
	 * @param connectionTimeoutInSeconds the connection timeout in seconds
	 * @return the builder
	 */
	WebServiceMessageSenderBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds);

	WebServiceMessageSenderBuilder withPasswordDecoder(Function<String, String> passwordDecoderFunction);

	WebServiceMessageSenderBuilder withKeyStore(KeyStoreResource keyStore);

	WebServiceMessageSenderBuilder withTrustStore(KeyStoreResource trustStore);

	WebServiceMessageSenderBuilder withSSLProtocolVersion(SSLProtocolVersion sslProtocolVersion);

	WebServiceMessageSender build();

}
