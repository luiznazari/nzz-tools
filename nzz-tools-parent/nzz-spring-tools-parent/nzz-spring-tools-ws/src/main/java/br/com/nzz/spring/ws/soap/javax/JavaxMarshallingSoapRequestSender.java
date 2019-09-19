package br.com.nzz.spring.ws.soap.javax;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.commons.concurrent.NzzFutures;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.soap.SoapWebService;
import lombok.extern.log4j.Log4j2;

/**
 * SOAP WebService Client. Responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (action). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * original error details.
 *
 * @author Luiz.Nazari
 * TODO move to a new common ws module.
 */
@Log4j2
public class JavaxMarshallingSoapRequestSender {

	private static final String SOAP_ACTION_HEADER = "SOAPAction";

	private final SoapWebService soapWebService;
	private final Marshaller marshaller;
	private final Unmarshaller unmarshaller;
	private String soapAction;
	private Environment environment;

	public JavaxMarshallingSoapRequestSender(SoapWebService soapWebService, Marshaller marshaller, Unmarshaller unmarshaller) {
		this.soapWebService = soapWebService;
		this.marshaller = marshaller;
		this.unmarshaller = unmarshaller;

		try {
			this.marshaller.setProperty("jaxb.fragment", Boolean.TRUE); // required to stop <?xml ... being added ?>
		} catch (PropertyException e) {
			throw new WebServiceException(e.getMessage(), e);
		}
	}

	public JavaxMarshallingSoapRequestSender withAction(String soapAction) {
		this.soapAction = soapAction;
		return this;
	}

	public JavaxMarshallingSoapRequestSender withEnvironment(Environment environment) {
		this.environment = environment;
		return this;
	}

	/**
	 * <p>Send an asynchronous request to the WebService with the specified payload object.</p>
	 * <p>No runtime exception will be thrown by this method, if any exception occurs while
	 * sending or receiving messages, it'll be returned in the {@link NzzCompletableFuture}'s
	 * callbacks.</p>
	 *
	 * @param <P>           the the payload object
	 * @param <R>           the type of the response object
	 * @param payload       the request payload object
	 * @param responseClass the response type class
	 * @return the response payload object
	 */
	public <P, R> NzzCompletableFuture<R> send(P payload, Class<R> responseClass) {
		return NzzFutures.resolve(() -> this.sendSync(payload, responseClass));
	}

	/**
	 * <p>Send a synchronous request to the WebService with the specified payload object.</p>
	 *
	 * @param <P>           the type of the payload object
	 * @param <R>           the type of the response object
	 * @param payload       the request payload object
	 * @param responseClass the response type class
	 * @return the response payload object
	 */
	public <P, R> R sendSync(P payload, Class<R> responseClass) {
		SOAPConnection soapConnection = null;

		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();

			return sendSoapRequest(soapConnection, payload, responseClass);

		} catch (SOAPException e) {
			log.error("Error occurred while sending SOAP Request! Make sure you have the correct endpoint URL and SOAPAction!");
			throw new WebServiceException("Error occurred while sending SOAP Request!", e);

		} catch (JAXBException | ParserConfigurationException e) {
			throw new WebServiceException("Error occurred while parsing the SOAP Payload!", e);

		} finally {
			this.closeSoapConnectionQuietly(soapConnection);
		}
	}

	private <P, R> R sendSoapRequest(SOAPConnection soapConnection, P payload, Class<R> responseClass) throws JAXBException, SOAPException, ParserConfigurationException {
		SOAPMessage soapRequest = createSOAPRequest(payload);
		this.logSoapMessage("Sending SOAP Request:", soapRequest);

		SOAPMessage soapResponse = soapConnection.call(soapRequest, this.soapWebService.getUrlWsdl(this.environment));
		this.logSoapMessage("Received SOAP Response:", soapResponse);

		return this.extractResponse(soapResponse, responseClass);
	}

	private <R> R extractResponse(SOAPMessage soapResponse, Class<R> responseClass) throws SOAPException, JAXBException {
		SOAPBody soapBody = soapResponse.getSOAPBody();
		SOAPFault soapFault = soapBody.getFault();

		if (soapFault != null) {
			String faultMsg = String.format("Received a SOAPFault! Code: %s. Message: %s.", soapFault.getFaultCode(), soapFault.getFaultString());
			throw new WebServiceException(faultMsg);
		}

		try {
			return responseClass.cast(this.unmarshaller.unmarshal(soapBody.extractContentAsDocument()));
		} catch (ClassCastException e) {
			throw new WebServiceException("Error occurred when un-marshalling soap response payload", e);
		}
	}

	private SOAPMessage createSOAPRequest(Object payload) throws SOAPException, JAXBException, ParserConfigurationException {
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		MimeHeaders headers = soapMessage.getMimeHeaders();
		if (this.soapAction != null) {
			headers.addHeader(SOAP_ACTION_HEADER, this.soapAction);
		}

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		this.marshaller.marshal(payload, document);
		soapMessage.getSOAPBody().addDocument(document);

		soapMessage.saveChanges();
		return soapMessage;
	}

	private void closeSoapConnectionQuietly(SOAPConnection soapConnection) {
		if (soapConnection != null) {
			try {
				soapConnection.close();
			} catch (SOAPException e) {
				log.error("Could not properly close the SOAP Connection.", e);
			}
		}
	}

	private void logSoapMessage(String message, SOAPMessage soapMessage) {
		if (log.isDebugEnabled()) {
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				soapMessage.writeTo(outputStream);
				String soapMessageXml = new String(outputStream.toByteArray());

				log.debug(message + "\n" + soapMessageXml);
			} catch (SOAPException | IOException e) {
				log.error("Could not log SOAPMessage.", e);
			}
		}
	}

}
