package br.com.luiz.nazari;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.openssl.PEMWriter;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Luiz.Nazari
 */
final class GeraCacerts {
	
	private static final int TIMEOUT_WS = 30;
	
	private static final int TLS_PORTA = 443;
	private static final String TLS_VERSAO = "TLSv1.2";
	
	private static final String JKS_SENHA = "changeit";
	private static final String JSSECACERTS_FILENAME = "cacerts";
	private static final String CAMINHO_CACERTS = "cacerts" + File.separatorChar;
	private static final String CAMINHO_CERTIFICADOS = CAMINHO_CACERTS + "certificados" + File.separatorChar;
	private static final String TEMPLATE_CAMINHO_CERTIFICADO = CAMINHO_CERTIFICADOS + "%s.crt";
	
	public static void main(String[] args) throws IOException {
		new GeraCacerts(args).gerar();
	}
	
	private KeyStore keyStore;
	private final String[] dominiosWs;
	
	private GeraCacerts(String[] dominiosWs) {
		this.dominiosWs = dominiosWs;
	}
	
	private void gerar() {
		try {
			char[] passphrase = JKS_SENHA.toCharArray();
			File cacerts = this.loadOrCreateCacerts();
			
			info("Loading KeyStore " + cacerts.getAbsolutePath() + "...");
			try (InputStream in = new FileInputStream(cacerts)) {
				keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				keyStore.load(in, passphrase);
			}
			
			for (String dominio : this.dominiosWs) {
				this.trustHost(dominio);
			}
			
			File cacertsToSave;
			if (cacerts.canWrite()) {
				cacertsToSave = cacerts;
			} else {
				cacertsToSave = new File(CAMINHO_CACERTS + JSSECACERTS_FILENAME);
			}
			try (FileOutputStream out = new FileOutputStream(cacertsToSave)) {
				keyStore.store(out, passphrase);
			}
			
			info(String.format("JSK \"%s\" generated in: \"%s\".", JSSECACERTS_FILENAME, cacertsToSave.getAbsolutePath()));
			info(String.format("Trusted certificates stored in: \"%s\".", new File(CAMINHO_CERTIFICADOS).getAbsolutePath()));
			
		} catch (Exception e) {
			error(e);
		}
	}
	
	private File loadOrCreateCacerts() {
		char sep = File.separatorChar;
		File dir = new File(System.getProperty("java.home") + sep + "lib" + sep + "security");
		File file = new File(dir, JSSECACERTS_FILENAME);
		if (!file.isFile()) {
			file = new File(CAMINHO_CACERTS + JSSECACERTS_FILENAME);
		}
		return file;
	}
	
	private void trustHost(String host) throws Exception {
		SSLContext context = SSLContext.getInstance(TLS_VERSAO);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);
		X509TrustManager defaultTrustManager = ( X509TrustManager ) tmf.getTrustManagers()[0];
		SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory factory = context.getSocketFactory();
		
		info("Opening connection to " + host + ":" + TLS_PORTA + "...");
		SSLSocket socket = ( SSLSocket ) factory.createSocket(host, TLS_PORTA);
		socket.setSoTimeout(TIMEOUT_WS * 1000);
		try {
			info("Starting SSL handshake...");
			socket.startHandshake();
			IOUtils.closeQuietly(socket);
			info("No errors, certificate is already trusted");
			
		} catch (SSLHandshakeException e) {
			/*
			 * PKIX path building failed:
			 * sun.security.provider.certpath.SunCertPathBuilderException:
			 * unable to find valid certification path to requested target
			 * Não tratado, pois sempre ocorre essa exceção quando o cacerts não está gerado.
			 */
			
		} catch (SSLException e) {
			error(e);
		}
		
		this.trustCertificates(host, tm.chain);
	}
	
	private void trustCertificates(String host, X509Certificate[] chain)
		throws NoSuchAlgorithmException, CertificateEncodingException, KeyStoreException, IOException {
		if (chain == null) {
			error("Could not obtain server certificate chain.");
			return;
		}
		
		info("Server sent " + chain.length + " certificate(s):");
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for (int i = 0; i < chain.length; i++) {
			X509Certificate cert = chain[i];
			sha1.update(cert.getEncoded());
			md5.update(cert.getEncoded());
			
			String alias = host + "-" + (i);
			keyStore.setCertificateEntry(alias, cert);
			this.saveCrtToFile(alias, cert);
			
			info("Added certificate to keystore '" + JSSECACERTS_FILENAME + "' using alias '" + alias + "'");
		}
	}
	
	private void saveCrtToFile(String alias, X509Certificate crt) throws IOException {
		// Obter conteúdo do certificado no formato PEM:
		StringWriter sw = new StringWriter();
		try (PEMWriter pw = new PEMWriter(sw)) {
			pw.writeObject(crt);
		}
		
		// Salvar arquivo certificado:
		File file = new File(String.format(TEMPLATE_CAMINHO_CERTIFICADO, alias));
		FileUtils.writeStringToFile(file, sw.toString(), Charsets.UTF_8);
	}
	
	private static class SavingTrustManager implements X509TrustManager {
		
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
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}
	
	private static void info(String log) {
		System.out.println("[INFO ] " + log);
	}
	
	private static void error(String log) {
		System.err.println("[ERROR] " + log);
	}
	
	private void error(Throwable t) {
		error(t.getMessage());
		t.printStackTrace();
	}
	
}