package br.com.nzz.spring.soap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.mime.MimeContainer;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.ResponseCreators;
import org.springframework.xml.transform.StringSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import br.com.nzz.spring.Environment;
import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.test.UnitTest;
import lombok.Setter;

import static org.springframework.ws.test.client.RequestMatchers.anything;

public class TypedSoapRequestTest extends UnitTest {

	private final SoapWebService soapWs;

	public TypedSoapRequestTest() {
		this.soapWs = new SoapWebService(
			"http://producao.envelope-soap-test.com",
			"http://homologacao.envelope-soap-test.com",
			"http://desenvolvimento.envelope-soap-test.com");
	}

	@Test
	public void mustSendSoapRequestToBothEnvironments() {
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.FALSE);
		envelopeSoap.withEnvironment(Environment.DEVELOPMENT).sendSync("false");
		mockServer.verify();

		mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.TRUE);
		envelopeSoap.withEnvironment(Environment.PRODUCTION).sendSync("true");
		mockServer.verify();
	}

	@Test
	public void shouldThrowExceptionWhenReceivesFaultResponse() {
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
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
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
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
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		MarshallingSoapRequest<String, Boolean> envelopeSoap = new MarshallingSoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer
			.expect(anything())
			.andRespond(ResponseCreators.withPayload(new StringSource("<content>false</content>")));
		marshaller.setResponse(Boolean.FALSE);

		try {
			envelopeSoap
				.withResponseExtractor((m, response) -> {
					throw new WebServiceInternalException("Lol", new IOException("fake.error.test"));
				})
				.sendSync("false");
			fail("Should have thrown an exception.");

		} catch (WebServiceException e) {
			Assert.assertEquals(NzzConstants.INTEGRATION_ERROR, e.getError().getMessageKey());
			Assert.assertEquals(NzzConstants.INTERNAL_ERROR, e.getError().getMessageParameters()[0]);

		} finally {
			mockServer.verify();
		}
	}

	private class SoapEnvelopTestJaxb2Marshaller extends Jaxb2Marshaller {

		@Setter
		private Object response;
		private JAXBContext jaxbContext = mock(JAXBContext.class);

		SoapEnvelopTestJaxb2Marshaller() {
			try {
				when(jaxbContext.createMarshaller()).thenReturn(mock(Marshaller.class));
				// when(jaxbContext.createUnmarshaller()).thenReturn(mock(Unmarshaller.class))
			} catch (JAXBException e) {
				fail(e);
			}
		}

		@Override
		public void marshal(Object graph, Result result) throws XmlMappingException {
		}

		@Override
		public Object unmarshal(Source source, MimeContainer mimeContainer) throws XmlMappingException {
			return this.response;
		}

		@Override
		public JAXBContext getJaxbContext() {
			return this.jaxbContext;
		}

	}

}