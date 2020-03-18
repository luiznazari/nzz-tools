package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceTransformerException;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.support.MarshallingUtils;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.xml.transform.StringSource;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import br.com.nzz.commons.concurrent.NzzCompletableFuture;
import br.com.nzz.commons.concurrent.NzzFutures;
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
 * @param <P> type of the payload object
 * @param <R> type of the response object
 * @author Luiz Felipe Nazari
 * @see SoapRequestFaultMessageResolver
 */
@Log4j2
public class MarshallingSoapRequest<P, R> extends WebServiceTemplateAbstractSoapRequest<P, R> implements TypedSoapRequest<P, R> {

	private final Jaxb2Marshaller marshaller;
	private WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction;

	public MarshallingSoapRequest(SoapWebService soapWebService, Jaxb2Marshaller marshaller) {
		super(soapWebService);
		this.marshaller = marshaller;
	}

	public MarshallingSoapRequest(String url, Jaxb2Marshaller marshaller) {
		this(SoapWebService.from(url), marshaller);
	}

	@Override
	public MarshallingSoapRequest<P, R> withAction(String soapAction) {
		super.withAction(soapAction);
		return this;
	}

	@Override
	public MarshallingSoapRequest<P, R> withEnvironment(Environment environment) {
		super.withEnvironment(environment);
		return this;
	}

	@Override
	public MarshallingSoapRequest<P, R> withResponseExtractor(WebServiceBiFunction<Jaxb2Marshaller, Object, R> responseExtractorFunction) {
		this.responseExtractorFunction = responseExtractorFunction;
		return this;
	}

	@Override
	public MarshallingSoapRequest<P, R> withMessageSender(WebServiceMessageSender webServiceMessageSender) {
		super.withMessageSender(webServiceMessageSender);
		return this;
	}

	@Override
	public MarshallingSoapRequest<P, R> withMessageSender(Consumer<WebServiceMessageSenderBuilder> webServiceMessageSenderBuilderConsumer) {
		super.withMessageSender(webServiceMessageSenderBuilderConsumer);
		return this;
	}

	@Override
	public MarshallingSoapRequest<P, R> doWithWebServiceGateway(Consumer<WebServiceGatewaySupport> wsGatewayConsumer) {
		super.doWithWebServiceGateway(wsGatewayConsumer);
		return this;
	}

	@Override
	public R sendSync(P payload) {
		return this.send(() -> {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);

			WebServiceMessageExtractor<R> messageResponseExtractor = new WebServiceMessageUnMarshallerResponseExtractor<>(
				this.marshaller, this.responseExtractorFunction);

			return this.wsGateway.getWebServiceTemplate().sendAndReceive(wsdlUrl,
				this.configureRequestCallback(payload),
				messageResponseExtractor);
		});
	}

	@Override
	public NzzCompletableFuture<R> sendXml(@Nonnull String payload) {
		return NzzFutures.resolve(() -> this.sendXmlSync(payload));
	}

	@Override
	public R sendXmlSync(@Nonnull String payload) {
		return this.send(() -> {
			String wsdlUrl = this.webService.getUrlWsdl(this.environment);

			SourceExtractor<R> sourceResponseExtractor = new SourceUnMarshallerResponseExtractor<>(
				this.marshaller, this.responseExtractorFunction);

			try {
				return this.wsGateway.getWebServiceTemplate().sendSourceAndReceive(wsdlUrl,
					new StringSource(payload),
					super.configureRequestCallback(),
					sourceResponseExtractor);
			} catch (WebServiceTransformerException e) {
				log.error("Failed to send the payload:\n\t" + payload + "\n\tMessage: " + e.getMessage(), e);
				throw e;
			}
		});
	}

	private WebServiceMessageCallback configureRequestCallback(P payload) {
		return message -> {
			MarshallingUtils.marshal(marshaller, payload, message);
			super.configureRequestCallback().doWithMessage(message);
		};
	}

}
