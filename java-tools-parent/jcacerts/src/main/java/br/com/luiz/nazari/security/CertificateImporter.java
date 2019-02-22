package br.com.luiz.nazari.security;

import br.com.luiz.nazari.security.config.CertificateImporterOptions;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.PEMWriter;

import javax.net.ssl.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author Luiz.Nazari
 *
 * // TODO refatorar código, separando/simplificando métodos e criando novas classes.
 * // Alterar a rotina para, primeiro, salvar todos os certificados e após isso
 * // aplicar decisões como: salvar em arquivo ou confiar (adicionar à KeyStore).
 */
class CertificateImporter {

	private static final int TIMEOUT_IN_SECONDS = 30;
	private static final String TEMPLATE_CRT_FILE = "%s.crt";

	private KeyStore keyStore;
	private final CertificateImporterOptions options;

	CertificateImporter(CertificateImporterOptions options) {
		this.options = options;
	}

	void importCertificates() {
		try {
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

			char[] passPhrase = this.options.getKeyStorePassword().toCharArray();

			for (String domain : this.options.getDomains()) {
				this.trustHost(domain);
			}

			if (this.options.isImportIntoKeyStore()) {
				File keyStoreFile = this.options.getKeyStoreFile();
				this.backupFile(keyStoreFile);
				info("Loading KeyStore " + keyStoreFile.getAbsolutePath() + "...");

				try (InputStream in = new FileInputStream(keyStoreFile)) {
					keyStore.load(in, passPhrase);
				}

				try (FileOutputStream out = new FileOutputStream(keyStoreFile)) {
					keyStore.store(out, passPhrase);
				}

				info(String.format("JSK saved/generated at: \"%s\".", keyStoreFile.getAbsolutePath()));
			}

			if (this.options.isSaveCertificates()) {
				info(String.format("Trusted certificates stored in: \"%s\".", this.options.getCertificatesOutputDirPath()));
			}

		} catch (Exception e) {
			error(e);
		}
	}

	private void backupFile(File file) throws IOException {
		if (file.exists()) {
			Path filePath = file.toPath();
			Path fileBkpPath = Paths.get(filePath.getParent().toString(), filePath.getFileName().toString() + ".bkp");
			Files.copy(filePath, fileBkpPath);
		}
	}

	private void trustHost(String domain) throws Exception {
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);

		X509TrustManager defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
		SavingTrustManager savingTrustManager = new SavingTrustManager(defaultTrustManager);

		SSLContext context = SSLContext.getInstance(this.options.getHttpSecurityProtocol());
		context.init(null, new TrustManager[]{savingTrustManager}, null);
		SSLSocketFactory factory = context.getSocketFactory();

		info("Opening connection to " + domain + ":" + this.options.getHttpPort() + "...");

		try (SSLSocket socket = (SSLSocket) factory.createSocket(domain, this.options.getHttpPort())) {
			socket.setSoTimeout((int) TimeUnit.SECONDS.toMillis(TIMEOUT_IN_SECONDS));
			info("Starting SSL handshake...");
			socket.startHandshake();
			info("No errors, certificate is already trusted");

		} catch (SSLHandshakeException e) {
			/*
			 * PKIX path building failed:
			 * sun.security.provider.certpath.SunCertPathBuilderException:
			 * unable to find valid certification path to requested target
			 * Não tratado, pois sempre ocorre essa exceção quando o cacerts não está gerado.
			 */

		} catch (SSLException e) {
			error(e);
		}

		this.trustCertificates(domain, savingTrustManager.getChain());
	}

	private void trustCertificates(String host, X509Certificate[] chain)
		throws NoSuchAlgorithmException, CertificateEncodingException, KeyStoreException, IOException {
		if (chain == null) {
			error("Could not obtain server certificate chain.");
			return;
		}

		info("Server sent " + chain.length + " certificate(s):");
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for (X509Certificate cert : chain) {
			sha1.update(cert.getEncoded());
			md5.update(cert.getEncoded());

			String alias = host + "~" + cert.getSerialNumber();

			if (this.options.isImportIntoKeyStore()) {
				keyStore.setCertificateEntry(alias, cert);
				info("Added certificate to keystore '" + this.options.getKeyStoreFileName() + "' using alias '" + alias + "'");
			}

			if (this.options.isSaveCertificates()) {
				this.saveCertificateToFile(alias, cert);
			}
		}
	}

	private void saveCertificateToFile(String alias, X509Certificate crt) throws IOException {
		// Convert certificate to PEM format.
		StringWriter sw = new StringWriter();
		try (PEMWriter pw = new PEMWriter(sw)) {
			pw.writeObject(crt);
		}

		String ctrDirPath = this.options.getCertificatesOutputDirPath() + File.separator;
		File crtFile = new File(ctrDirPath, String.format(TEMPLATE_CRT_FILE, alias));
		FileUtils.writeStringToFile(crtFile, sw.toString(), StandardCharsets.UTF_8);
	}

	private static void info(String log) {
		System.out.println("[INFO ] " + log);
	}

	private static void error(String log) {
		System.err.println("[ERROR] " + log);
	}

	private void error(Throwable throwable) {
		error(throwable.getMessage());
		throwable.printStackTrace();
	}

}