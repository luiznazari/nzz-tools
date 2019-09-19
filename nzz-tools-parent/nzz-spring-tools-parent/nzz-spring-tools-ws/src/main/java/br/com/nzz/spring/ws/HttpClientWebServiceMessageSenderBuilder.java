package br.com.nzz.spring.ws;

import org.apache.commons.io.IOUtils;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import br.com.nzz.spring.NzzSpringWsConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.model.KeyStoreResource;
import br.com.nzz.spring.model.SSLProtocolVersion;

/**
 * Creates an {@link WebServiceMessageSender} with the following security configuration:
 * <ul>
 * <li>Read timeout</li>
 * <li>Connection timeout</li>
 * <li>SSL protocol version</li>
 * <li>KeyStore</li>
 * <li>TrustStore</li>
 * </ul>
 *
 * @author Luiz.Nazari
 */
public class HttpClientWebServiceMessageSenderBuilder implements WebServiceMessageSenderBuilder {

	private static final int DEFAULT_TIMEOUT_IN_SECONDS = 60;

	private static final String LOCALHOST = "localhost";
	private static final String KEY_STORE = "KeyStore";
	private static final String TRUST_STORE = "TrustStore";

	private Function<String, String> passwordDecoder;
	private KeyStoreResource keyStore;
	private KeyStoreResource trustStore;
	private Integer readTimeoutInSeconds;
	private Integer connectionTimeoutInSeconds;
	private SSLProtocolVersion sslProtocolVersion;

	public HttpClientWebServiceMessageSenderBuilder() {
		this(Function.identity());
	}

	public HttpClientWebServiceMessageSenderBuilder(Function<String, String> passwordDecoder) {
		this.passwordDecoder = passwordDecoder;
	}

	@Override
	public WebServiceMessageSenderBuilder withReadTimeoutInSeconds(Integer readTimeoutInSeconds) {
		this.readTimeoutInSeconds = readTimeoutInSeconds;
		return this;
	}

	@Override
	public WebServiceMessageSenderBuilder withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
		return this;
	}

	@Override
	public WebServiceMessageSenderBuilder withPasswordDecoder(Function<String, String> passwordDecoderFunction) {
		this.passwordDecoder = passwordDecoderFunction;
		return this;
	}

	@Override
	public WebServiceMessageSenderBuilder withKeyStore(KeyStoreResource keyStore) {
		this.keyStore = keyStore;
		return this;
	}

	@Override
	public WebServiceMessageSenderBuilder withTrustStore(KeyStoreResource trustStore) {
		this.trustStore = trustStore;
		return this;
	}

	@Override
	public WebServiceMessageSenderBuilder withSSLProtocolVersion(SSLProtocolVersion sslProtocolVersion) {
		this.sslProtocolVersion = sslProtocolVersion;
		return this;
	}

	@Override
	public WebServiceMessageSender build() {
		HttpUrlConnectionMessageSender messageSender;

		if (this.hasHttpsConfiguration()) {
			HttpsUrlConnectionMessageSender httpsMessageSender = new HttpsUrlConnectionMessageSender();
			this.configureSsl(httpsMessageSender);
			this.configureKeyStores(httpsMessageSender);

			// Prevent "java.security.cert.CertificateException: No name matching localhost found".
			httpsMessageSender.setHostnameVerifier((hostname, sslSession) -> hostname.equals(LOCALHOST));
			messageSender = httpsMessageSender;

		} else {
			messageSender = new HttpUrlConnectionMessageSender();
		}

		this.configureTimeouts(messageSender);

		return messageSender;
	}

	private boolean hasHttpsConfiguration() {
		return this.sslProtocolVersion != null || this.keyStore != null || this.trustStore != null;
	}

	private void configureTimeouts(HttpUrlConnectionMessageSender messageSender) {
		messageSender.setReadTimeout(getTimeoutDuration(this.readTimeoutInSeconds));
		messageSender.setConnectionTimeout(getTimeoutDuration(this.connectionTimeoutInSeconds));
	}

	private static Duration getTimeoutDuration(Integer timeoutInSeconds) {
		return Duration.ofSeconds(Optional.ofNullable(timeoutInSeconds).orElse(DEFAULT_TIMEOUT_IN_SECONDS));
	}

	private void configureSsl(HttpsUrlConnectionMessageSender messageSender) {
		if (this.sslProtocolVersion != null) {
			messageSender.setSslProtocol(this.sslProtocolVersion.getProtocolName());
		}
	}

	private void configureKeyStores(HttpsUrlConnectionMessageSender messageSender) {
		KeyStoreResource currentKeyStoreResource = null;
		try {
			currentKeyStoreResource = this.keyStore;
			this.configureKeyStore(messageSender);

			currentKeyStoreResource = this.trustStore;
			this.configureTrustStore(messageSender);

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

	private void configureKeyStore(HttpsUrlConnectionMessageSender messageSender) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
		if (this.keyStore != null) {
			KeyStore keyStoreInstance = loadKeyStore(this.keyStore);
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStoreInstance, decodePassword(this.keyStore.getPassword()));

			messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
		}
	}

	private void configureTrustStore(HttpsUrlConnectionMessageSender messageSender) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		if (this.trustStore != null) {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(loadKeyStore(this.trustStore));

			messageSender.setTrustManagers(trustManagerFactory.getTrustManagers());
		}
	}

	private KeyStore loadKeyStore(KeyStoreResource keyStoreResource) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		KeyStore keyStoreInstance = KeyStore.getInstance(keyStoreResource.getType().name());
		keyStoreInstance.load(keyStoreResource.getInputStream(), decodePassword(keyStoreResource.getPassword()));
		IOUtils.closeQuietly(keyStoreResource.getInputStream());
		return keyStoreInstance;
	}

	private char[] decodePassword(String encodedPassword) {
		return this.passwordDecoder.apply(encodedPassword).toCharArray();
	}

}
