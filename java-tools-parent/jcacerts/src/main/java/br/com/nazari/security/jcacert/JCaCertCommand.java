package br.com.nazari.security.jcacert;

import picocli.CommandLine;

/**
 * @author Luiz.Nazari
 */
@CommandLine.Command(
	name = "jcacert",
	version = SecurityConstants.JCACERT_COMMAND_VERSION,
	description = "Java public root CA certificates Importer v" + SecurityConstants.JCACERT_COMMAND_VERSION)
final class JCaCertCommand extends CertificateImporterOptions implements Runnable {

	@CommandLine.Option(names = "--version", description = "Displays version information.",
		versionHelp = true, defaultValue = "true")
	private boolean versionOption = true;

	@Override
	public void run() {
		CertificateImporterOptions options = this;
		new CertificateImporter(options).importCertificates();
	}

	public static void main(String[] args) {
		CommandLine.run(new JCaCertCommand(), args);
	}

}