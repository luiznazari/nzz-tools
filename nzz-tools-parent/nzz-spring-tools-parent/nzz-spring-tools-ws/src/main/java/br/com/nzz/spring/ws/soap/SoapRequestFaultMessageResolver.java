package br.com.nzz.spring.ws.soap;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapMessage;

import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;

/**
 * Soap fault messages handler. Always will throw a {@link WebServiceException}
 * with original fault code and fault string.
 *
 * @author Luiz Felipe Nazari
 */
final class SoapRequestFaultMessageResolver implements FaultMessageResolver {

	private static final String UNKNOWN_ERROR = "The request SOAP WebService returned an invalid SOAP message.";

	@Override
	public void resolveFault(WebServiceMessage message) {
		try {
			SoapRequestLogger.logSoapMessage("Received SOAP fault response", message);

			SoapMessage soapMessage = (SoapMessage) message;
			SoapFault fault = soapMessage.getSoapBody().getFault();
			String code = fault.getFaultCode().getLocalPart();
			String failMessage = fault.getFaultStringOrReason();

			throw new WebServiceException(NzzConstants.INTEGRATION_ERROR)
				.parameters(code, failMessage);

		} catch (NullPointerException | ClassCastException e) {
			/*
			 * Just for safety's sake: those errors will not be thrown.
			 * However, we must guarantee that exceptions will not occur
			 * while handling SOAP fault messages.
			 */
			throw new WebServiceException(NzzConstants.INTEGRATION_ERROR, e)
				.parameters(NzzConstants.INTERNAL_ERROR, UNKNOWN_ERROR);
		}
	}

}