package br.com.nazari.security.jcacert;

import lombok.Getter;
import lombok.Setter;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

@Getter
@Setter
public class CertificateImporterOptions {

	private static final String CACERTS_FILENAME = "cacerts";
	private static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";
	private static final String DEFAULT_SECURITY_PROTOCOL_PORT = "443";
	private static final String DEFAULT_SECURITY_PROTOCOL_VERSION = "TLSv1.2";

	@CommandLine.Option(
		names = {"-S", "--httpSecurityProtocol"}, defaultValue = DEFAULT_SECURITY_PROTOCOL_VERSION,
		description = "Security protocol version used when stabilizing connection with the servers.")
	private String httpSecurityProtocol = DEFAULT_SECURITY_PROTOCOL_VERSION;

	@CommandLine.Option(
		names = {"-P", "--httpPort"}, defaultValue = DEFAULT_SECURITY_PROTOCOL_PORT,
		description = "The port used when stabilizing connection with the servers.")
	private int httpPort = Integer.valueOf(DEFAULT_SECURITY_PROTOCOL_PORT);

	@CommandLine.Option(
		names = {"-k", "--keyStorePath"},
		description = "Absolute path of the target Key Store. Defaults to ${java.home}/lib/security/cacerts.")
	private String keyStorePath;

	@CommandLine.Option(
		names = {"-p", "--keyStorePassword"}, defaultValue = DEFAULT_KEY_STORE_PASSWORD,
		description = "Password of the target Key Store.")
	private String keyStorePassword = DEFAULT_KEY_STORE_PASSWORD;

	@CommandLine.Option(
		names = {"-o", "--outDirPath"},
		description = "Output directory to save the downloaded server certificates.")
	private String certificatesOutputDirPath;

	@CommandLine.Option(
		names = {"-A", "--importIntoKeyStore"}, defaultValue = "true")
	private boolean importIntoKeyStore = false;

	@CommandLine.Parameters(
		arity = "1..*", paramLabel = "domains",
		description = "Domains to download root CA certificates from.")
	private String[] domains;

	public String[] getDomains() {
		return Optional.ofNullable(this.domains).orElse(new String[0]);
	}

	public boolean isSaveCertificates() {
		return this.certificatesOutputDirPath != null;
	}

	public File getSrcKeyStoreFile() {
		if (this.keyStorePath != null) {
			return new File(this.keyStorePath);
		}
		File dir = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "security");
		return new File(dir, CACERTS_FILENAME);
	}

	public String getKeyStoreFileName() {
		String realKeyStorePath = getSrcKeyStoreFile().getAbsolutePath();
		int lastSeparatorIndex = realKeyStorePath.lastIndexOf(File.separatorChar);
		if (lastSeparatorIndex != -1) {
			return realKeyStorePath.substring(lastSeparatorIndex);
		}
		return realKeyStorePath;
	}

}
