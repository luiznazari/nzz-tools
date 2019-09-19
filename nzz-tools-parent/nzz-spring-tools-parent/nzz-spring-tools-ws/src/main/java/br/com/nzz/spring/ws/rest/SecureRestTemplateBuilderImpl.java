package br.com.nzz.spring.ws.rest;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.ProxyScheme;
import br.com.nzz.spring.model.SSLProtocolVersion;
import br.com.nzz.spring.ws.HttpsClientBuilder;
import br.com.nzz.spring.ws.SecureHttpClientBuilder;

/**
 * Creates an {@link RestTemplate} with the following security configuration:
 * <ul>
 * <li>Read timeout</li>
 * <li>Connect timeout</li>
 * <li>Connection timeout</li>
 * <li>Proxy</li>
 * <li>HTTP Basic Auth</li>
 * <li>SSL protocol version</li>
 * <li>KeyStore</li>
 * <li>TrustStore</li>
 * </ul>
 *
 * @author Luiz.Nazari
 */
public class SecureRestTemplateBuilderImpl implements SecureRestTemplateBuilder {

	private Integer readTimeoutInSeconds;
	private Integer connectTimeoutInSeconds;
	private Integer connectionTimeoutInSeconds;
	private SecureHttpClientBuilder secureHttpClientBuilder;

	public SecureRestTemplateBuilderImpl() {
		this.secureHttpClientBuilder = new HttpsClientBuilder();
	}

	public SecureRestTemplateBuilderImpl(Function<String, String> passwordDecoderFunction) {
		this.secureHttpClientBuilder = new HttpsClientBuilder(passwordDecoderFunction);
	}

	@Override
	public SecureRestTemplateBuilderImpl withReadTimeoutInSeconds(Integer readTimeoutInSeconds) {
		this.readTimeoutInSeconds = readTimeoutInSeconds;
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withConnectTimeoutInSeconds(Integer connectTimeoutInSeconds) {
		this.connectTimeoutInSeconds = connectTimeoutInSeconds;
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withPasswordDecoder(Function<String, String> passwordDecoderFunction) {
		this.secureHttpClientBuilder.withPasswordDecoder(passwordDecoderFunction);
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withProxy(String hostname, int port) {
		this.secureHttpClientBuilder.withProxy(hostname, port);
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withProxy(String hostname, int port, ProxyScheme scheme) {
		this.secureHttpClientBuilder.withProxy(hostname, port, scheme);
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withHttpBasicAuth(String user, String password) {
		this.secureHttpClientBuilder.withHttpBasicAuth(user, password);
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withKeyStore(KeyStoreResource keyStore) {
		this.secureHttpClientBuilder.withKeyStore(keyStore);
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withTrustStore(KeyStoreResource trustStore) {
		this.secureHttpClientBuilder.withTrustStore(trustStore);
		return this;
	}

	@Override
	public SecureRestTemplateBuilderImpl withSSLProtocolVersion(SSLProtocolVersion sslProtocolVersion) {
		this.secureHttpClientBuilder.withSslProtocolVersion(sslProtocolVersion);
		return this;
	}

	@Override
	public RestTemplate build() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		this.configureTimeouts(clientHttpRequestFactory);
		clientHttpRequestFactory.setHttpClient(this.secureHttpClientBuilder.build());

		return new RestTemplate(clientHttpRequestFactory);
	}

	private void configureTimeouts(HttpComponentsClientHttpRequestFactory clientHttpRequestFactory) {
		if (this.readTimeoutInSeconds != null)
			clientHttpRequestFactory.setReadTimeout(getTimeoutMilliseconds(this.readTimeoutInSeconds));
		if (this.connectTimeoutInSeconds != null)
			clientHttpRequestFactory.setConnectTimeout(getTimeoutMilliseconds(this.connectTimeoutInSeconds));
		if (this.connectionTimeoutInSeconds != null)
			clientHttpRequestFactory.setConnectionRequestTimeout(getTimeoutMilliseconds(this.connectionTimeoutInSeconds));
	}

	private int getTimeoutMilliseconds(Integer readTimeoutInSeconds) {
		return (int) TimeUnit.SECONDS.toMillis((long) readTimeoutInSeconds);
	}

}
