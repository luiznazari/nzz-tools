package br.com.nzz.spring.ws.soap;

import org.junit.Test;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;
import org.springframework.xml.transform.StringSource;

import java.util.stream.Stream;

import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.spring.model.SSLProtocolVersion;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.test.UnitTest;

import static org.junit.Assert.assertTrue;

public class SimpleSoapRequestTest extends UnitTest {

	private final SoapWebService soapWs;

	public SimpleSoapRequestTest() {
		this.soapWs = new SoapWebService(
			"http://producao.envelope-soap-test.com",
			"http://homologacao.envelope-soap-test.com",
			"http://desenvolvimento.envelope-soap-test.com");
	}

	@Test
	public void mustSendSoapRequest() throws WebServiceInternalException {
		final String payloadXml = "<element><randomTag>Wololo</randomTag></element>";

		SoapRequest soapRequest = SoapRequest.from(soapWs)
			.withEnvironment(Environment.DEVELOPMENT);

		MockWebServiceServer mockServer = MockWebServiceServer
			.createServer(((SimpleSoapRequest) soapRequest).getWsGateway());
		mockServer.expect(RequestMatchers.payload(new StringSource(payloadXml)));

		soapRequest.sendSync(payloadXml);

		mockServer.verify();
	}

	@Test
	public void shouldSendSoapRequestWithCustomHttpsSecurityParameters() {
		SimpleSoapRequest soapRequest = new SimpleSoapRequest(soapWs)
			.withMessageSender(messageSenderBuilder -> messageSenderBuilder
				.withReadTimeoutInSeconds(1)
				.withConnectionTimeoutInSeconds(1)
				.withSSLProtocolVersion(SSLProtocolVersion.TLS_V1)
				.withKeyStore(null)
				.withTrustStore(null)
			);

		boolean hasBuiltHttpsMessageSender = Stream.of(soapRequest.getWsGateway()
			.getWebServiceTemplate()
			.getMessageSenders())
			.anyMatch(messageSender -> messageSender.getClass() == HttpsUrlConnectionMessageSender.class);
		assertTrue(hasBuiltHttpsMessageSender);
	}

	@Test
	public void shouldSendSoapRequestWithCustomHttpsSecurityParameters2() {
		WebServiceMessageSender webServiceMessageSender = mock(WebServiceMessageSender.class);

		SimpleSoapRequest soapRequest = new SimpleSoapRequest(soapWs)
			.withMessageSender(webServiceMessageSender);

		boolean hasBuiltHttpsMessageSender = Stream.of(soapRequest.getWsGateway()
			.getWebServiceTemplate()
			.getMessageSenders())
			.anyMatch(messageSender -> messageSender == webServiceMessageSender);
		assertTrue(hasBuiltHttpsMessageSender);
	}

}