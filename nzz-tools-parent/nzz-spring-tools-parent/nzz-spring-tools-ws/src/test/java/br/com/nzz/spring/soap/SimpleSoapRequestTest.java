package br.com.nzz.spring.soap;

import org.junit.Test;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.xml.transform.StringSource;

import br.com.nzz.spring.Environment;
import br.com.nzz.test.UnitTest;

public class SimpleSoapRequestTest extends UnitTest {

	private final SoapWebService soapWs;

	public SimpleSoapRequestTest() {
		this.soapWs = new SoapWebService(
			"http://producao.envelope-soap-test.com",
			"http://homologacao.envelope-soap-test.com",
			"http://desenvolvimento.envelope-soap-test.com");
	}

	@Test
	public void mustSendSoapRequest() {
		SimpleSoapRequest envelopeSoap = new SimpleSoapRequest(soapWs);
		String payloadXml = "<element><randomTag>Wololo</randomTag></element>";

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer.expect(RequestMatchers.payload(new StringSource(payloadXml)));
		envelopeSoap.withEnvironment(Environment.DEVELOPMENT).sendSync(payloadXml);
		mockServer.verify();
	}

}