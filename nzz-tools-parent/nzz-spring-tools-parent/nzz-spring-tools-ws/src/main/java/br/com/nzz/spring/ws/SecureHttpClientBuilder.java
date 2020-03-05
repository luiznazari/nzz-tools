package br.com.nzz.spring.ws;

import org.apache.http.client.HttpClient;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.ProxyScheme;
import br.com.nzz.spring.model.SSLProtocolVersion;

/**
 * Creates an {@link HttpClient} with the following security configuration:
 * <ul>
 * <li>Proxy</li>
 * <li>HTTP Basic Auth</li>
 * <li>SSL protocol version</li>
 * <li>KeyStore</li>
 * <li>TrustStore</li>
 * </ul>
 *
 * @author Luiz.Nazari
 */
public interface SecureHttpClientBuilder {

	SecureHttpClientBuilder withPasswordDecoder(UnaryOperator<String> passwordDecoderFunction);

	/**
	 * <p>Sets the limit timeout in seconds to wait reading the WebService response.
	 * <p>The timeout will be caused if a connection is established but there is no
	 * response within the time limit.
	 *
	 * @param readTimeoutInSeconds the read timeout in seconds
	 * @return the builder
	 */
	SecureHttpClientBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds);

	/**
	 * <p>Sets the limit connection in seconds while trying to connect to the WebService.
	 * <p>The timeout will be caused if no connection is established within the time limit.
	 *
	 * @param connectionTimeoutInSeconds the connection timeout in seconds
	 * @return the builder
	 */
	SecureHttpClientBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds);

	SecureHttpClientBuilder withProxy(String hostname, int port);

	SecureHttpClientBuilder withProxy(String hostname, int port, ProxyScheme scheme);

	SecureHttpClientBuilder withHttpBasicAuth(String user, String password);

	SecureHttpClientBuilder withKeyStore(KeyStoreResource keyStore);

	SecureHttpClientBuilder withTrustStore(KeyStoreResource trustStore);

	SecureHttpClientBuilder withSslProtocolVersion(SSLProtocolVersion sslProtocolVersion);

	HttpClient build();

	static SecureHttpClientBuilder custom() {
		return new HttpsClientBuilder();
	}

	static SecureHttpClientBuilder custom(UnaryOperator<String> passwordDecoder) {
		return new HttpsClientBuilder(passwordDecoder);
	}

}
