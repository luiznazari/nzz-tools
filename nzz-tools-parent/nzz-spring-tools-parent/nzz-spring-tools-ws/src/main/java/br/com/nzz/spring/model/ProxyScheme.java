package br.com.nzz.spring.model;


import java.net.Proxy;

/**
 * @author Luiz.Nazari
 * @see Proxy.Type
 */
public enum ProxyScheme {

	HTTP,
	HTTPS;

	public String getSchemeName() {
		return this.name().toLowerCase();
	}
}
