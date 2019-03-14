package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.NzzWsConstants;
import br.com.nzz.spring.ws.exception.WebServiceException;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapMessage;

/**
 * Soap fault messages handler. Always will throw a {@link WebServiceException}
 * with original fault code and fault string.
 *
 * @author Luiz Felipe Nazari
 */
final class SoapEnvelopeFaultMessageResolver implements FaultMessageResolver {

	private static final String UNKNOWN_ERROR = "The request SOAP WebService returned an invalid SOAP message.";

	@Override
	public void resolveFault(WebServiceMessage message) {
		try {
			SoapRequestLogger.logSoapMessage("Received SOAP fault response", message);

			SoapMessage soapMessage = (SoapMessage) message;
			SoapFault fault = soapMessage.getSoapBody().getFault();
			String code = fault.getFaultCode().getLocalPart();
			String failMessage = fault.getFaultStringOrReason();

			throw new WebServiceException(NzzWsConstants.SOAP_INTEGRATION_ERROR, code, failMessage);

		} catch (NullPointerException | ClassCastException e) {
			/*
			 * Just for safety's sake: those errors will not be thrown.
			 * However, we must guarantee that exceptions will not occur
			 * while handling SOAP fault messages.
			 */
			throw new WebServiceException(e, NzzWsConstants.SOAP_INTEGRATION_ERROR,
				NzzWsConstants.INTERNAL_ERROR, UNKNOWN_ERROR);
		}
	}

}