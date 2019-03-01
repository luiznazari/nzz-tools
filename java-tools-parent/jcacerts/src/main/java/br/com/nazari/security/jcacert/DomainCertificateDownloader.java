package br.com.nazari.security.jcacert;

import lombok.extern.log4j.Log4j2;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
class DomainCertificateDownloader {

	private static final int TIMEOUT_IN_SECONDS = 30;

	private final KeyStore keyStore;
	private final int defaultHttpPort;
	private final String httpSecurityProtocol;

	DomainCertificateDownloader(KeyStore keyStore, CertificateImporterOptions options) {
		this.keyStore = keyStore;
		this.defaultHttpPort = options.getHttpPort();
		this.httpSecurityProtocol = options.getHttpSecurityProtocol();
	}

	List<X509Certificate> downloadCertificates(String domain) throws Exception {
		String realDomain = domain;
		int httpPort = this.defaultHttpPort;

		String[] domainAndPort = domain.split(":");
		if (domainAndPort.length > 1) {
			realDomain = domainAndPort[0];
			httpPort = this.extractDomainPort(domainAndPort[1]).orElse(this.defaultHttpPort);
		}

		return this.downloadCertificates(realDomain, httpPort);
	}

	private Optional<Integer> extractDomainPort(String port) {
		try {
			return Optional.of(Integer.valueOf(port));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	private List<X509Certificate> downloadCertificates(String domain, int httpPort)
		throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateEncodingException {
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);

		X509TrustManager defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
		SavingTrustManager savingTrustManager = new SavingTrustManager(defaultTrustManager);

		SSLContext context = SSLContext.getInstance(this.httpSecurityProtocol);
		context.init(null, new TrustManager[]{savingTrustManager}, null);
		SSLSocketFactory factory = context.getSocketFactory();

		log.debug(String.format("Opening connection to %s:%d...", domain, httpPort));

		try (SSLSocket socket = (SSLSocket) factory.createSocket(domain, httpPort)) {
			socket.setSoTimeout((int) TimeUnit.SECONDS.toMillis(TIMEOUT_IN_SECONDS));
			log.debug("Starting SSL handshake...");
			socket.startHandshake();
			log.debug("No errors, certificate is already trusted");

		} catch (SSLHandshakeException e) {
			/*
			 * PKIX path building failed:
			 * sun.security.provider.certpath.SunCertPathBuilderException:
			 * unable to find valid certification path to requested target
			 *
			 * Nothing to do. This exception will always be thrown when
			 * the KeyStore is not yet generated.
			 */

		} catch (SSLException | SocketException e) {
			log.error(e);
		}

		return this.extractCertificates(savingTrustManager.getChain());
	}

	private List<X509Certificate> extractCertificates(X509Certificate[] chain)
		throws NoSuchAlgorithmException, CertificateEncodingException {

		if (chain == null) {
			log.info("Could not obtain server certificates chain.");
			return Collections.emptyList();
		}

		log.info("Server sent " + chain.length + " certificate(s):");
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		List<X509Certificate> certificates = new ArrayList<>();
		for (X509Certificate cert : chain) {
			sha1.update(cert.getEncoded());
			md5.update(cert.getEncoded());
			certificates.add(cert);
		}

		return certificates;
	}

}
