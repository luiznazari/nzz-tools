package br.com.nzz.spring.ws;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import br.com.nzz.spring.NzzSpringWsConstants;
import br.com.nzz.spring.exception.WebServiceException;
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
public class HttpsClientBuilder implements SecureHttpClientBuilder {

	private static final String KEY_STORE = "KeyStore";
	private static final String TRUST_STORE = "TrustStore";

	private UnaryOperator<String> passwordDecoder;
	private HttpHost proxyHost;
	private Integer readTimeoutInSeconds;
	private Integer connectionTimeoutInSeconds;
	private KeyStoreResource keyStore;
	private KeyStoreResource trustStore;
	private Credentials basicCredentials;
	private SSLProtocolVersion sslProtocolVersion;

	public HttpsClientBuilder() {
		this(UnaryOperator.identity());
	}

	public HttpsClientBuilder(UnaryOperator<String> passwordDecoder) {
		this.passwordDecoder = passwordDecoder;
	}

	@Override
	public HttpsClientBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds) {
		this.readTimeoutInSeconds = readTimeoutInSeconds;
		return this;
	}

	@Override
	public HttpsClientBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
		return this;
	}

	@Override
	public HttpsClientBuilder withPasswordDecoder(UnaryOperator<String> passwordDecoderFunction) {
		this.passwordDecoder = passwordDecoderFunction;
		return this;
	}

	@Override
	public HttpsClientBuilder withProxy(String hostname, int port) {
		this.proxyHost = new HttpHost(hostname, port);
		return this;
	}

	@Override
	public HttpsClientBuilder withProxy(String hostname, int port, ProxyScheme scheme) {
		this.proxyHost = new HttpHost(hostname, port,
			Optional.ofNullable(scheme).orElse(ProxyScheme.HTTP).getSchemeName());
		return this;
	}

	@Override
	public HttpsClientBuilder withHttpBasicAuth(String user, String password) {
		this.basicCredentials = new UsernamePasswordCredentials(user, password);
		return this;
	}

	@Override
	public HttpsClientBuilder withKeyStore(KeyStoreResource keyStore) {
		this.keyStore = keyStore;
		return this;
	}

	@Override
	public HttpsClientBuilder withTrustStore(KeyStoreResource trustStore) {
		this.trustStore = trustStore;
		return this;
	}

	@Override
	public HttpsClientBuilder withSslProtocolVersion(SSLProtocolVersion sslProtocolVersion) {
		this.sslProtocolVersion = sslProtocolVersion;
		return this;
	}

	@Override
	public HttpClient build() {
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		this.configureTimeouts(requestBuilder);

		org.apache.http.impl.client.HttpClientBuilder httpClientBuilder = HttpClients.custom()
			.addInterceptorFirst(contentLengthHeaderRemover())
			.setDefaultRequestConfig(requestBuilder.build());

		if (hasHttpsConfiguration()) {
			httpClientBuilder.setSSLContext(this.buildSSLContext());
		}

		if (this.proxyHost != null) {
			httpClientBuilder.setProxy(this.proxyHost);
		}

		this.configureHttpAuth(httpClientBuilder);

		return httpClientBuilder.build();
	}

	private void configureTimeouts(RequestConfig.Builder requestBuilder) {
		if (this.readTimeoutInSeconds != null)
			requestBuilder.setConnectTimeout(getTimeoutMilliseconds(this.readTimeoutInSeconds));
		if (this.connectionTimeoutInSeconds != null)
			requestBuilder.setConnectionRequestTimeout(getTimeoutMilliseconds(this.connectionTimeoutInSeconds));
	}

	private int getTimeoutMilliseconds(Integer readTimeoutInSeconds) {
		return (int) TimeUnit.SECONDS.toMillis((long) readTimeoutInSeconds);
	}

	private boolean hasHttpsConfiguration() {
		return this.sslProtocolVersion != null || this.keyStore != null || this.trustStore != null;
	}

	private void configureHttpAuth(org.apache.http.impl.client.HttpClientBuilder httpClientBuilder) {
		if (this.basicCredentials != null) {
			Credentials decodedBasicCredentials = new UsernamePasswordCredentials(
				this.basicCredentials.getUserPrincipal().getName(),
				this.passwordDecoder.apply(this.basicCredentials.getPassword()));

			CredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
			basicCredentialsProvider.setCredentials(AuthScope.ANY, decodedBasicCredentials);
			httpClientBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
		}
	}

	private SSLContext buildSSLContext() {
		try {
			SSLContextBuilder sslContextBuilder = SSLContexts.custom();
			this.configureSsl(sslContextBuilder);
			this.configureKeyStores(sslContextBuilder);

			return sslContextBuilder.build();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new WebServiceException(NzzSpringWsConstants.COULD_NOT_CREATE_SSL_CONTEXT, e)
				.parameters(e.getMessage());
		}
	}

	private void configureSsl(SSLContextBuilder sslContextBuilder) {
		if (this.sslProtocolVersion != null) {
			sslContextBuilder.setProtocol(this.sslProtocolVersion.getProtocolName());
		}
	}

	private void configureKeyStores(SSLContextBuilder sslContextBuilder) {
		KeyStoreResource currentKeyStoreResource = null;
		try {
			currentKeyStoreResource = this.keyStore;
			this.configureKeyStore(sslContextBuilder);

			currentKeyStoreResource = this.trustStore;
			this.configureTrustStore(sslContextBuilder);

		} catch (IOException | CertificateException | NoSuchAlgorithmException e) {
			throw new WebServiceException(NzzSpringWsConstants.COULD_NOT_LOAD_KEYSTORE, e)
				.parameters(getDescription(currentKeyStoreResource), e.getMessage());

		} catch (KeyStoreException e) {
			throw new WebServiceException(NzzSpringWsConstants.COULD_NOT_INSTANTIATE_KEYSTORE_TYPE, e)
				.parameters(getDescription(currentKeyStoreResource), currentKeyStoreResource.getType(), e.getMessage());

		} catch (UnrecoverableKeyException e) {
			throw new WebServiceException(NzzSpringWsConstants.COULD_NOT_LOAD_KEYSTORE_WITH_PASSWORD, e)
				.parameters(getDescription(currentKeyStoreResource), e.getMessage());
		}
	}

	private String getDescription(KeyStoreResource currentKeyStoreResource) {
		return currentKeyStoreResource == this.trustStore ? TRUST_STORE : KEY_STORE;
	}

	private void configureKeyStore(SSLContextBuilder sslContextBuilder) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
		if (this.keyStore != null) {
			KeyStore keyStoreInstance = loadKeyStore(this.keyStore);
			sslContextBuilder.loadKeyMaterial(keyStoreInstance, decodePassword(this.keyStore.getCertificatePassword()));
		}
	}

	private void configureTrustStore(SSLContextBuilder sslContextBuilder) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		if (this.trustStore != null) {
			KeyStore ksTrustStore = loadKeyStore(this.trustStore);
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(ksTrustStore);

			sslContextBuilder.loadTrustMaterial(ksTrustStore, null);
		}
	}

	private KeyStore loadKeyStore(KeyStoreResource keyStoreResource) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		KeyStore keyStoreInstance = KeyStore.getInstance(keyStoreResource.getType().name());
		try (InputStream keyStoreInputStream = keyStoreResource.getInputStream()) {
			keyStoreInstance.load(keyStoreInputStream, decodePassword(keyStoreResource.getPassword()));
		}
		return keyStoreInstance;
	}

	private char[] decodePassword(String encodedPassword) {
		return this.passwordDecoder.apply(encodedPassword).toCharArray();
	}

	private HttpRequestInterceptor contentLengthHeaderRemover() {
		// Prevents error org.apache.http.protocol.RequestContent's org.apache.http.ProtocolException: Content-Length header already present.
		return (HttpRequest request, HttpContext context) ->
			request.removeHeaders(HTTP.CONTENT_LEN);
	}

}
