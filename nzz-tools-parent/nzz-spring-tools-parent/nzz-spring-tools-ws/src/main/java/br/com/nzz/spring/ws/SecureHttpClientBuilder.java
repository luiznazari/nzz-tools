package br.com.nzz.spring.ws;

import org.apache.http.client.HttpClient;

import java.util.function.Function;

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

	SecureHttpClientBuilder withPasswordDecoder(Function<String, String> passwordDecoderFunction);

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

	static SecureHttpClientBuilder custom(Function<String, String> passwordDecoder) {
		return new HttpsClientBuilder(passwordDecoder);
	}

}
