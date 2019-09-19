package br.com.nzz.spring.model;

/**
 * @author Luiz.Nazari
 */
public enum KeyStoreType {

	/**
	 * Java Key Store. You can find this file at sun.security.provider.JavaKeyStore.
	 * This keystore is Java specific, it usually has an extension of jks. This type
	 * of keystore can contain private keys and certificates, but it cannot be used to
	 * store secret keys. Since it's a Java specific keystore, so it cannot be used in
	 * other programming languages.
	 */
	JKS,

	/**
	 * JCE key store. You can find this file at com.sun.crypto.provider.JceKeyStore.
	 * This keystore has an extension of jceks. The entries which can be put in the
	 * JCEKS keystore are private keys, secret keys and certificates.
	 */
	JCEKS,

	/**
	 * this is a standard keystore type which can be used in Java and other languages.
	 * You can find this keystore implementation at sun.security.pkcs12.PKCS12KeyStore.
	 * It usually has an extension of p12 or pfx. You can store private keys, secret keys
	 * and certificates on this type.
	 */
	PKCS12,

	/**
	 * PKCS11 is a hardware keystore type. It servers an interface for the Java library to
	 * connect with hardware keystore devices such as Luna, nCipher. You can find this
	 * implementation at sun.security.pkcs11.P11KeyStore. When you load the keystore, you no
	 * need to create a specific provider with specific configuration. This keystore can store
	 * private keys, secret keys and certificates. When loading the keystore, the entries will
	 * be retrieved from the keystore and then converted into software entries.
	 */
	PKCS11;

}
