package br.com.nzz.spring.soap;

import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.xml.transform.StringSource;

import br.com.nzz.spring.Environment;
import br.com.nzz.spring.exception.WebServiceException;
import lombok.extern.log4j.Log4j2;

/**
 * SOAP WebService Client. Responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (action). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * original error details.
 *
 * @author Luiz Felipe Nazari
 * @see SoapEnvelopeFaultMessageResolver
 */
@Log4j2
public class SimpleSoapRequest extends AbstractSoapRequest<String, String> {

	public SimpleSoapRequest(String url) {
		super(url);
	}

	public SimpleSoapRequest(SoapWebService soapWebService) {
		super(soapWebService);
	}

	@Override
	public SimpleSoapRequest withAction(String soapAction) {
		super.withAction(soapAction);
		return this;
	}

	@Override
	public SimpleSoapRequest withEnvironment(Environment environment) {
		super.withEnvironment(environment);
		return this;
	}

	@Override
	public SimpleSoapRequest withRequestTimeoutInSeconds(Integer requestTimeoutInSeconds) {
		super.withConnectionTimeoutInSeconds(requestTimeoutInSeconds);
		return this;
	}

	@Override
	public SimpleSoapRequest withConnectionTimeoutInSeconds(Integer connectionTimeoutInSeconds) {
		super.withConnectionTimeoutInSeconds(connectionTimeoutInSeconds);
		return this;
	}

	@Override
	public String sendSync(String payload) throws WebServiceException {
		return super.send(() -> {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);

			SourceExtractor<String> sourceExtractor = new SourceStringResponseExtractor();

			return this.wsGateway.getWebServiceTemplate().sendSourceAndReceive(wsdlUrl,
				new StringSource(payload),
				configureRequestCallback(),
				sourceExtractor);
		});
	}

}
