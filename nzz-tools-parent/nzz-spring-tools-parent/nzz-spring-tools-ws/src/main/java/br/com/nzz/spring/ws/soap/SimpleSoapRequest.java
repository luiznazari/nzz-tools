package br.com.nzz.spring.ws.soap;

import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.xml.transform.StringSource;

import java.util.function.Consumer;

import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.WebServiceMessageSenderBuilder;
import lombok.extern.log4j.Log4j2;

/**
 * SOAP WebService Client. Responsible for configuring and handling SOAP requests
 * with ou without and SOAP method (action). If any exception are captured while,
 * sending or receiving messages, a {@link WebServiceException} will be thrown with
 * original error details.
 *
 * @author Luiz Felipe Nazari
 * @see SoapRequestFaultMessageResolver
 */
@Log4j2
public class SimpleSoapRequest extends WebServiceTemplateAbstractSoapRequest<String, String> implements SoapRequest {

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
	public SimpleSoapRequest withMessageSender(WebServiceMessageSender webServiceMessageSender) {
		super.withMessageSender(webServiceMessageSender);
		return this;
	}

	@Override
	public SimpleSoapRequest withMessageSender(Consumer<WebServiceMessageSenderBuilder> webServiceMessageSenderBuilderConsumer) {
		super.withMessageSender(webServiceMessageSenderBuilderConsumer);
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
