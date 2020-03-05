package br.com.nzz.spring.ws.rest;

import org.springframework.web.client.RestTemplate;

import java.util.function.UnaryOperator;

import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.ProxyScheme;
import br.com.nzz.spring.model.SSLProtocolVersion;

/**
 * @author Luiz.Nazari
 */
public interface SecureRestTemplateBuilder {

	/**
	 * Sets the limit timeout in seconds to wait reading the WebService response.
	 *
	 * @param readTimeoutInSeconds the read timeout in seconds
	 * @return the builder
	 */
	SecureRestTemplateBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds);

	/**
	 * Sets the limit timeout in seconds to try to connect to the WebService.
	 *
	 * @param connectTimeoutInSeconds the connect timeout in seconds
	 * @return the builder
	 */
	SecureRestTemplateBuilder withConnectTimeoutInSeconds(Integer connectTimeoutInSeconds);

	/**
	 * Sets the limit connection in seconds while trying to connect to the WebService.
	 *
	 * @param connectionTimeoutInSeconds the connection timeout in seconds
	 * @return the builder
	 */
	SecureRestTemplateBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds);

	SecureRestTemplateBuilder withPasswordDecoder(UnaryOperator<String> passwordDecoderFunction);

	SecureRestTemplateBuilder withProxy(String hostname, int port);

	SecureRestTemplateBuilder withProxy(String hostname, int port, ProxyScheme scheme);

	SecureRestTemplateBuilder withHttpBasicAuth(String user, String password);

	SecureRestTemplateBuilder withKeyStore(KeyStoreResource keyStore);

	SecureRestTemplateBuilder withTrustStore(KeyStoreResource trustStore);

	SecureRestTemplateBuilder withSSLProtocolVersion(SSLProtocolVersion sslProtocolVersion);

	RestTemplate build();

	static SecureRestTemplateBuilder custom() {
		return new SecureRestTemplateBuilderImpl();
	}

	static SecureRestTemplateBuilder custom(UnaryOperator<String> passwordDecoderFunction) {
		return new SecureRestTemplateBuilderImpl(passwordDecoderFunction);
	}

}
