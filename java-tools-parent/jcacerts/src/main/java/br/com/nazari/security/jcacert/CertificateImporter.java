package br.com.nazari.security.jcacert;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.PEMWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @author Luiz.Nazari
 */
@Log4j2
class CertificateImporter {

	private static final String TEMPLATE_CRT_FILE = "%s.crt";

	private final CertificateImporterOptions options;

	CertificateImporter(CertificateImporterOptions options) {
		this.options = options;
	}

	void importCertificates() {
		try {
			final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.loadSrcKeyStoreInto(keyStore);

			Map<String, X509Certificate> hostsCertificates = this.downloadDomainsCertificatesInto(keyStore);

			if (this.options.isImportIntoKeyStore()) {
				this.importCertificatesToDestinationKeyStore(keyStore);
			}

			if (this.options.isSaveCertificates()) {
				this.saveCertificates(hostsCertificates);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Download certificates for given domains.
	 *
	 * @param keyStore The managed KeyStore to be updated.
	 * @return the downloaded certificates with respective aliases.
	 * @throws Exception If an error occurs while connecting to servers or downloading the certificates.
	 */
	private Map<String, X509Certificate> downloadDomainsCertificatesInto(KeyStore keyStore) throws Exception {
		DomainCertificateDownloader domainCertificateDownloader = new DomainCertificateDownloader(keyStore, this.options);

		Map<String, X509Certificate> hostsCertificates = new HashMap<>();
		for (String domain : this.options.getDomains()) {
			for (X509Certificate crt : domainCertificateDownloader.downloadCertificates(domain)) {
				String alias = String.format("%s~%s", domain, crt.getSerialNumber());
				hostsCertificates.put(alias, crt);
				keyStore.setCertificateEntry(alias, crt);
				log.info(String.format("Certificate added into keystore using alias \"%s\"", alias));
			}
		}

		return hostsCertificates;
	}

	/**
	 * Load source KeyStore into the managed KeyStore.
	 *
	 * @param keyStore The managed KeyStore to be updated.
	 * @throws IOException              If an I/O error occurs while copying the file.
	 * @throws CertificateException     If any of the certificates in the keystore could not be loaded.
	 * @throws NoSuchAlgorithmException If the algorithm used to check the integrity of the keystore cannot be found.
	 */
	private void loadSrcKeyStoreInto(KeyStore keyStore) throws IOException, CertificateException, NoSuchAlgorithmException {
		char[] passPhrase = this.options.getSrcKeyStorePassword().toCharArray();

		File srcKeyStoreFile = this.options.getSrcKeyStoreFile();
		log.info(String.format("Loading KeyStore %s...", srcKeyStoreFile.getAbsolutePath()));

		if (srcKeyStoreFile.exists()) {
			try (InputStream in = new FileInputStream(srcKeyStoreFile)) {
				keyStore.load(in, passPhrase);
			}
		} else {
			keyStore.load(null, null);
		}
	}

	/**
	 * Import all KeyStore's trusted certificates to destination KeyStore, thus trusting all imported certificate CA's.
	 *
	 * @param keyStore The KeyStore containing all downloaded certificates.
	 * @throws IOException              If an I/O error occurs while creating backup file or opening output stream to destination KeyStore.
	 * @throws CertificateException     if any of the certificates included in the keystore data could not be stored while storing certificates into destination KeyStore.
	 * @throws NoSuchAlgorithmException If the appropriate data integrity algorithm could not be found while storing certificates into destination KeyStore.
	 * @throws KeyStoreException        If the keystore has not been initialized (loaded).
	 */
	private void importCertificatesToDestinationKeyStore(KeyStore keyStore) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		char[] passPhrase = this.options.getDestKeyStorePassword().toCharArray();

		File destinationKeyStoreFile = this.options.getDestKeyStoreFile();
		this.backupFile(destinationKeyStoreFile);

		try (FileOutputStream out = new FileOutputStream(destinationKeyStoreFile)) {
			keyStore.store(out, passPhrase);
		}

		log.info(String.format("JSK saved/generated at: \"%s\".", destinationKeyStoreFile.getAbsolutePath()));
	}

	/**
	 * Create a backup file of the destination KeyStore with '.bkp' suffix.
	 *
	 * @param file The destination file.
	 * @throws IOException If an I/O error occurs while copying the file.
	 */
	private void backupFile(File file) throws IOException {
		if (file.exists()) {
			Path filePath = file.toPath();
			Path fileBkpPath = Paths.get(filePath.getParent().toString(), filePath.getFileName().toString() + ".bkp");
			Files.copy(filePath, fileBkpPath, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Save all downloaded certificates into a local folder.
	 *
	 * @param hostsCertificates The certificates to save.
	 * @throws IOException If an I/O error occurs while saving the certificates.
	 */
	private void saveCertificates(Map<String, X509Certificate> hostsCertificates) throws IOException {
		for (Map.Entry<String, X509Certificate> crtEntry : hostsCertificates.entrySet()) {
			saveCertificateToFile(crtEntry.getKey(), crtEntry.getValue());
		}

		File outDir = new File(this.options.getCertificatesOutputDirPath());
		log.info(String.format("Trusted certificates stored in: \"%s\".", outDir.getAbsolutePath()));
	}

	/**
	 * Save the certificate into a local file named with certificate's alias.
	 *
	 * @param alias The file name.
	 * @param crt   The certificate to save.
	 * @throws IOException If an I/O error occurs while saving the certificate.
	 */
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

}