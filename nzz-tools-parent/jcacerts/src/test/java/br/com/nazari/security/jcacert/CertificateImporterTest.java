package br.com.nazari.security.jcacert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@Ignore
@RunWith(BlockJUnit4ClassRunner.class)
public class CertificateImporterTest {

	@Test
	public void shouldImportCertificatesFromDomain() {
		CertificateImporterOptions options = new CertificateImporterOptions();
		options.setDomains(new String[]{"www.google.com.br"});
		options.setCertificatesOutputDirPath("tempCerts");

		new CertificateImporter(options).importCertificates();
	}

}