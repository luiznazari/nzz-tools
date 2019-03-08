package br.com.nzz.security.jcacert;

import lombok.AccessLevel;
import lombok.Getter;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

class SavingTrustManager implements X509TrustManager {

	@Getter(AccessLevel.PACKAGE)
	private X509Certificate[] chain;
	private final X509TrustManager tm;

	SavingTrustManager(X509TrustManager tm) {
		this.tm = tm;
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		this.chain = chain;
		tm.checkServerTrusted(chain, authType);
	}

}