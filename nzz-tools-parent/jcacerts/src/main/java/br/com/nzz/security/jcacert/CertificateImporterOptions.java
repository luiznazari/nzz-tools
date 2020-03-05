package br.com.nzz.security.jcacert;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

@Getter
@Setter
abstract class CertificateImporterOptions {

	private static final String CACERTS_FILENAME = "cacerts";
	private static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";
	private static final String DEFAULT_SECURITY_PROTOCOL_PORT = "443";
	private static final String DEFAULT_SECURITY_PROTOCOL_VERSION = "TLSv1.2";

	@CommandLine.Option(
		names = {"-k", "--srcKeyStore"},
		description = "Absolute path of the target Key Store. Defaults to ${java.home}/lib/security/cacerts.")
	@Getter(AccessLevel.NONE)
	private String srcKeyStorePath;

	@CommandLine.Option(
		names = {"-p", "--srcKeyStorePassword"}, defaultValue = DEFAULT_KEY_STORE_PASSWORD,
		description = "Password of the target Key Store. Defaults to " + DEFAULT_KEY_STORE_PASSWORD + ".")
	private String srcKeyStorePassword = DEFAULT_KEY_STORE_PASSWORD;

	@CommandLine.Option(
		names = {"-K", "--destKeyStore"},
		description = "Absolute path of the destination Key Store. Defaults to --srcKeyStorePath.")
	@Getter(AccessLevel.NONE)
	private String destKeyStorePath;

	@CommandLine.Option(
		names = {"-P", "--destKeyStorePassword"},
		description = "Password of the destination Key Store. Defaults to --srcKeyStorePassword.")
	private String destKeyStorePassword;

	@CommandLine.Option(
		names = {"-o", "--outCrtDir"},
		description = "Absolute directory path where the downloaded certificates will be saved.")
	private String certificatesOutputDirPath;

	@CommandLine.Option(
		names = {"-i", "--import"}, defaultValue = "false",
		description = "Defines if the downloaded certificates will be imported into the destination KeyStore. Defaults to false.")
	private boolean importIntoKeyStore = false;

	@CommandLine.Parameters(
		arity = "1..*", paramLabel = "domains",
		description = "Domains to download root CA certificates from.")
	private String[] domains;

	@CommandLine.Option(
		names = {"--httpSecurityProtocol"}, defaultValue = DEFAULT_SECURITY_PROTOCOL_VERSION,
		description = "Security protocol version used when stabilizing connection with the servers.")
	private String httpSecurityProtocol = DEFAULT_SECURITY_PROTOCOL_VERSION;

	@CommandLine.Option(
		names = {"--httpPort"}, defaultValue = DEFAULT_SECURITY_PROTOCOL_PORT,
		description = "The default port used when stabilizing connection with the servers.")
	private int httpPort = Integer.valueOf(DEFAULT_SECURITY_PROTOCOL_PORT);

	public String[] getDomains() {
		return Optional.ofNullable(this.domains).orElse(new String[0]);
	}

	public boolean isSaveCertificates() {
		return this.certificatesOutputDirPath != null;
	}

	public String getDestKeyStorePath() {
		if (this.destKeyStorePath == null) {
			return this.getSrcKeyStorePath();
		}
		return this.destKeyStorePath;
	}

	public String getDestKeyStorePassword() {
		if (this.destKeyStorePassword == null) {
			return this.getSrcKeyStorePassword();
		}
		return this.destKeyStorePassword;
	}

	public String getSrcKeyStorePath() {
		if (this.srcKeyStorePath != null) {
			return this.srcKeyStorePath;
		}
		return System.getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator + CACERTS_FILENAME;
	}

	public File getSrcKeyStoreFile() {
		return new File(this.getSrcKeyStorePath());
	}

	public File getDestKeyStoreFile() {
		return new File(this.getDestKeyStorePath());
	}

}
