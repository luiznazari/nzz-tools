package br.com.nzz.spring.model;

import lombok.Getter;

/**
 * @author Luiz.Nazari
 * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SSLContext">
 * Java Cryptography Architecture Standard Algorithm Name Documentation</a>
 */
public enum SSLProtocolVersion {

	/**
	 * Supports some version of SSL; may support other versions.
	 */
	SSL("SSL"),

	/**
	 * Supports SSL version 2 or later; may support other versions.
	 */
	SSL_V2("SSLv2"),

	/**
	 * Supports SSL version 3; may support other versions.
	 */
	SSL_V3("SSLv3"),

	/**
	 * Supports some version of TLS; may support other versions.
	 */
	TLS("TLS"),

	/**
	 * Supports RFC 2246: TLS version 1.0; may support other versions.
	 */
	TLS_V1("TLSv1"),

	/**
	 * Supports RFC 4346: TLS version 1.1; may support other versions.
	 */
	TLS_V1_1("TLSv1.1"),

	/**
	 * Supports RFC 5246: TLS version 1.2; may support other versions.
	 */
	TLS_V1_2("TLSv1.2");

	@Getter
	private final String protocolName;

	SSLProtocolVersion(String protocolName) {
		this.protocolName = protocolName;
	}

}
