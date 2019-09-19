package br.com.nzz.spring.ws;

import org.apache.http.client.HttpClient;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import br.com.nzz.spring.NzzSpringWsConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.ProxyScheme;
import br.com.nzz.spring.model.SSLProtocolVersion;

/**
 * Creates an {@link WebServiceMessageSender} with the following security configuration:
 * <ul>
 * <li>Read timeout</li>
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
public class HttpComponentsWebServiceMessageSenderBuilder implements WebServiceMessageSenderBuilder {

	private Integer readTimeoutInSeconds;
	private Integer connectionTimeoutInSeconds;
	private final SecureHttpClientBuilder httpClientBuilder;

	public HttpComponentsWebServiceMessageSenderBuilder() {
		this.httpClientBuilder = new HttpsClientBuilder();
	}

	public HttpComponentsWebServiceMessageSenderBuilder(Function<String, String> passwordDecoderFunction) {
		this.httpClientBuilder = new HttpsClientBuilder(passwordDecoderFunction);
	}

	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds) {
		this.readTimeoutInSeconds = readTimeoutInSeconds;
		return this;
	}

	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
		return this;
	}

	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withPasswordDecoder(Function<String, String> passwordDecoderFunction) {
		this.httpClientBuilder.withPasswordDecoder(passwordDecoderFunction);
		return this;
	}

	//	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withProxy(String hostname, int port) {
		this.httpClientBuilder.withProxy(hostname, port);
		return this;
	}

	//	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withProxy(String hostname, int port, ProxyScheme scheme) {
		this.httpClientBuilder.withProxy(hostname, port, scheme);
		return this;
	}

	//	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withHttpBasicAuth(String user, String password) {
		this.httpClientBuilder.withHttpBasicAuth(user, password);
		return this;
	}

	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withKeyStore(KeyStoreResource keyStore) {
		this.httpClientBuilder.withKeyStore(keyStore);
		return this;
	}

	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withTrustStore(KeyStoreResource trustStore) {
		this.httpClientBuilder.withTrustStore(trustStore);
		return this;
	}

	@Override
	public HttpComponentsWebServiceMessageSenderBuilder withSSLProtocolVersion(SSLProtocolVersion sslProtocolVersion) {
		this.httpClientBuilder.withSslProtocolVersion(sslProtocolVersion);
		return this;
	}

	@Override
	public WebServiceMessageSender build() {
		HttpClient httpClient = this.httpClientBuilder.build();

		HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(httpClient);
		this.configureTimeouts(messageSender);
		this.finalizeProperties(messageSender);
		return messageSender;
	}

	private void configureTimeouts(HttpComponentsMessageSender messageSender) {
		if (this.readTimeoutInSeconds != null)
			messageSender.setReadTimeout(getTimeoutMilliseconds(this.readTimeoutInSeconds));
		if (this.readTimeoutInSeconds != null)
			messageSender.setConnectionTimeout(getTimeoutMilliseconds(this.connectionTimeoutInSeconds));
	}

	private int getTimeoutMilliseconds(Integer readTimeoutInSeconds) {
		return (int) TimeUnit.SECONDS.toMillis((long) readTimeoutInSeconds);
	}

	private void finalizeProperties(HttpComponentsMessageSender messageSender) {
		try {
			messageSender.afterPropertiesSet();
		} catch (Exception e) {
			throw new WebServiceException(NzzSpringWsConstants.COULD_NOT_CREATE_MESSAGE_SENDER, e)
				.parameters(e.getMessage());
		}
	}

}
