package br.com.luiz.nazari.security;

import br.com.luiz.nazari.security.jcacert.CertificateImporter;
import br.com.nazari.security.jcacert.CertificateImporterOptions;
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