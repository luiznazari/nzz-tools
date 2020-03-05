package br.com.nzz.spring.ws.soap;

import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.ResponseCreators;
import org.springframework.xml.transform.StringSource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

import javax.xml.transform.Source;

import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.soap.xml.SoapTestRequestType;
import br.com.nzz.spring.ws.soap.xml.SoapTestResponseType;
import br.com.nzz.test.UnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.ws.test.client.RequestMatchers.anything;

public class MarshallingSoapRequestTest extends UnitTest {

	private final SoapWebService soapWs;

	public MarshallingSoapRequestTest() {
		this.soapWs = new SoapWebService(
			"http://producao.envelope-soap-test.com",
			"http://homologacao.envelope-soap-test.com",
			"http://desenvolvimento.envelope-soap-test.com");
	}

	@Test
	public void mustSendSoapRequestToBothEnvironments() {
		MockJaxb2Marshaller marshaller = new MockJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWebServiceGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.FALSE);
		envelopeSoap.withEnvironment(Environment.DEVELOPMENT).sendSync("false");
		mockServer.verify();

		mockServer = MockWebServiceServer.createServer(envelopeSoap.getWebServiceGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.TRUE);
		envelopeSoap.withEnvironment(Environment.PRODUCTION).sendSync("true");
		mockServer.verify();
	}

	@Test
	public void shouldThrowExceptionWhenReceivesFaultResponse() {
		MockJaxb2Marshaller marshaller = new MockJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWebServiceGateway());
		mockServer
			.expect(anything())
			.andRespond(ResponseCreators.withClientOrSenderFault("SOAP FAULT MESSAGE", Locale.getDefault()));

		try {
			envelopeSoap.sendSync("");
			fail("Should have thrown an exception.");
		} catch (WebServiceException e) {
			// Expected exception.
		} finally {
			mockServer.verify();
		}
	}

	@Test
	public void mustExtractMessageWithCustomResponseExtractor() {
		MockJaxb2Marshaller marshaller = new MockJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWebServiceGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.FALSE);

		WebServiceBiFunction<Jaxb2Marshaller, Object, Boolean> randomResponseExtractor = (m, response) -> new Random().nextBoolean();

		envelopeSoap
			.withResponseExtractor(randomResponseExtractor)
			.sendSync("false");

		mockServer.verify();
	}

	@Test
	public void shouldHandleInternalErrorWithCustomResponseExtractor() {
		MockJaxb2Marshaller marshaller = new MockJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> typedSoapRequest = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(typedSoapRequest.getWebServiceGateway());
		mockServer
			.expect(anything())
			.andRespond(ResponseCreators.withPayload(new StringSource("<content>false</content>")));
		marshaller.setResponse(Boolean.FALSE);

		try {
			typedSoapRequest
				.withResponseExtractor((m, response) -> {
					throw new WebServiceInternalException("Lol", new IOException("fake.error.test"));
				})
				.sendSync("false");

			fail("Should have thrown an exception.");

		} catch (WebServiceException e) {
			assertEquals(NzzConstants.INTEGRATION_ERROR, e.toErrorMessage().getMessageKey());
			assertEquals(NzzConstants.INTERNAL_ERROR, e.toErrorMessage().getMessageParameters()[0]);

		} finally {
			mockServer.verify();
		}
	}

	public void shouldSendSoapRequestMarshallingAndUnMarshallingPayloads() throws WebServiceInternalException {
		final Source requestPayloadSource = new StringSource(
			"<soapTestRequest>" +
				"<username>luiz.nazari</username>" +
				"<password>123456</password>" +
				"</soapTestRequest>");

		final Source responsePayloadSource = new StringSource(
			"<soapTestResponse>" +
				"<name>Luiz Felipe Nazari</name>" +
				"<nickname></nickname>" +
				"<birthDate>28/11/1994</birthDate>" +
				"</soapTestResponse>");

		Jaxb2Marshaller marshaller = TypedSoapRequest.createMarshaller(SoapTestRequestType.class);
		TypedSoapRequest<SoapTestRequestType, SoapTestResponseType> typeTypedSoapRequest = TypedSoapRequest.from(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer
			.createServer(typeTypedSoapRequest.getWebServiceGateway());
		mockServer
			.expect(RequestMatchers.payload(requestPayloadSource))
			.andRespond(ResponseCreators.withPayload(responsePayloadSource));

		SoapTestRequestType request = new SoapTestRequestType();
		request.setUsername("luiz.nazari");
		request.setPassword("123456");
		SoapTestResponseType soapTestResponse = typeTypedSoapRequest.sendSync(request);

		mockServer.verify();
		assertNotNull(soapTestResponse);
		assertEquals("Luiz Felipe Nazari", soapTestResponse.getName());
		assertEquals("", soapTestResponse.getNickname());
		assertEquals(LocalDate.of(1994, 11, 28), soapTestResponse.getBirthDate());
	}

}