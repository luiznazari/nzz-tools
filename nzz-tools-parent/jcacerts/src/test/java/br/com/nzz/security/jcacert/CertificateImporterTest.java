package br.com.nzz.security.jcacert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(BlockJUnit4ClassRunner.class)
public class CertificateImporterTest {

	@Test
	public void shouldImportCertificatesFromDomain() throws IOException {
		CertificateImporterOptions options = new CertificateImporterOptions() {
		};
		options.setDomains(new String[]{"www.google.com.br"});
		Path tempDirectory = Files.createTempDirectory("nzz-test-output");
		options.setCertificatesOutputDirPath(tempDirectory.toAbsolutePath().toString());

		new CertificateImporter(options).importCertificates();

		File[] certificates = tempDirectory.toFile().listFiles();
		assertNotNull(certificates);
		assertTrue(certificates.length > 0);
	}

}