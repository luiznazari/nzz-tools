package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.NzzWsConstants;
import br.com.nzz.spring.ws.exception.WebServiceException;
import br.com.nzz.spring.ws.exception.WebServiceInternalException;
import br.com.senior.test.UnitTest;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.mime.MimeContainer;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.ResponseCreators;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import static br.com.nzz.spring.ws.Environment.DEVELOPMENT;
import static br.com.nzz.spring.ws.Environment.PRODUCTION;
import static org.springframework.ws.test.client.RequestMatchers.anything;

public class SoapRequestTest extends UnitTest {

	private final SoapWebService soapWs;

	public SoapRequestTest() {
		this.soapWs = new SoapWebService(
			"http://producao.envelope-soap-test.com",
			"http://homologacao.envelope-soap-test.com");
	}

	@Test
	public void mustSendSoapRequestToBothEnvironments() {
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		SoapRequest<String, Boolean> envelopeSoap = new SoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.FALSE);
		envelopeSoap.environment(DEVELOPMENT).send("false");
		mockServer.verify();

		mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.TRUE);
		envelopeSoap.environment(PRODUCTION).send("true");
		mockServer.verify();
	}

	@Test
	public void shouldThrowExceptionWhenReceivesFaultResponse() {
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		SoapRequest<String, Boolean> envelopeSoap = new SoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer
			.expect(anything())
			.andRespond(ResponseCreators.withClientOrSenderFault("SOAP FAULT MESSAGE", Locale.getDefault()));

		try {
			envelopeSoap.send(StringUtils.EMPTY);
			//fail("Should have thrown an exception.");
		} catch (WebServiceException e) {
			// Expected exception.
		} finally {
			mockServer.verify();
		}
	}

	@Test
	public void mustExtractMessageWithCustomResponseExtractor() {
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		SoapRequest<String, Boolean> envelopeSoap = new SoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer.expect(anything());
		marshaller.setResponse(Boolean.FALSE);

		WebServiceBiFunction<Jaxb2Marshaller, Object, Boolean> randomResponseExtractor = (m, response) -> new Random().nextBoolean();

		envelopeSoap
			.responseExtractor(randomResponseExtractor)
			.send("false");

		mockServer.verify();
	}

	@Test
	public void shouldHandleInternalErrorWithCustomResponseExtractor() {
		SoapEnvelopTestJaxb2Marshaller marshaller = new SoapEnvelopTestJaxb2Marshaller();
		SoapRequest<String, Boolean> envelopeSoap = new SoapRequest<>(soapWs, marshaller);

		MockWebServiceServer mockServer = MockWebServiceServer.createServer(envelopeSoap.getWsGateway());
		mockServer
			.expect(anything())
			.andRespond(ResponseCreators.withPayload(new StringSource("<content>false</content>")));
		marshaller.setResponse(Boolean.FALSE);

		try {
			envelopeSoap
				.responseExtractor((m, response) -> {
					throw new WebServiceInternalException("Lol", new IOException("fake.error.test"));
				})
				.send("false");
			//fail("Should have thrown an exception.");

		} catch (WebServiceException e) {
			Assert.assertEquals(NzzWsConstants.SOAP_INTEGRATION_ERROR, e.getError().getMessageKey());
			Assert.assertEquals(NzzWsConstants.INTERNAL_ERROR, e.getError().getMessageParameters()[0]);

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
				//fail(e);
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