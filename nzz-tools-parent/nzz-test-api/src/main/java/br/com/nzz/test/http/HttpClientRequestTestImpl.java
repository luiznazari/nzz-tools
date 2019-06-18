package br.com.nzz.test.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.jspare.vertx.DataObjectConverter;
import org.junit.Assert;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The base implementation of {@link HttpClientRequestTest}.
 *
 * @author Luiz.Nazari
 * @see HttpClientRequestTest
 */
@Slf4j
public class HttpClientRequestTestImpl implements HttpClientRequestTest {

	private static final Handler<Buffer> PRETTY_PRINT_BODY_HANDLER = buffer -> {
		String response = buffer.toString();
		String prettyResponse;

		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(response, Object.class);
			prettyResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

		} catch (IOException e) {
			prettyResponse = response;
			Assert.fail(e.getMessage());
		}

		log.debug("Response:\n" + prettyResponse);
	};

	private static final Handler<HttpClientResponse> DEFAULT_RESPONSE_HANDLER = response -> {
		if (response.statusCode() == 500 || response.statusCode() == 400) {
			// When a 'probably' unwanted request error occur, pretty print the response's
			// body content as a human-readable format.
			response.bodyHandler(PRETTY_PRINT_BODY_HANDLER);
		}
	};

	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String CONTENT_FORM_DATA = "multipart/form-data";
	private static final String CONTENT_JSON = "application/json";

	private final HttpClientRequest delegate;
	private BiConsumer<HttpClientResponse, Handler<HttpClientResponse>> responseHandlerWrapper = (response, handler) -> {
	};
	private Handler<HttpClientResponse> testResponseHandler = response -> {
	};

	HttpClientRequestTestImpl(HttpClientRequest httpClientRequest) {
		this.delegate = httpClientRequest;
		this.delegate.handler(response -> {
			DEFAULT_RESPONSE_HANDLER.handle(response);
			responseHandlerWrapper.accept(response, testResponseHandler);
		});
	}

	@Override
	public HttpClientRequestTest handlerWrapper(BiConsumer<HttpClientResponse, Handler<HttpClientResponse>> responseHandlerConsummer) {
		this.responseHandlerWrapper = responseHandlerConsummer;
		return this;
	}

	@Override
	public HttpClientRequestTest json(String property, Object value) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.put(property, value);
		return this.json(jsonObject);
	}

	@Override
	public HttpClientRequestTest jsonOf(Object object) {
		JsonObject jsonObject = DataObjectConverter.toJson(object, new JsonObject());
		return this.json(jsonObject);
	}

	@Override
	public HttpClientRequestTest json(JsonObject jsonObject) {
		String jsonString = jsonObject.encode();
		this.delegate
			.putHeader(HEADER_CONTENT_TYPE, CONTENT_JSON)
			.putHeader(HEADER_CONTENT_LENGTH, String.valueOf(jsonString.length()))
			.write(jsonString);
		return this;
	}

	@Override
	public HttpClientRequestTest formData(Consumer<FormDataBuilder> formDataBuilderConsumer) {
		FormDataBuilder formDataBuilder = new FormDataBuilder();
		formDataBuilderConsumer.accept(formDataBuilder);
		return this.formData(formDataBuilder);
	}

	@Override
	public HttpClientRequestTest formData(FormDataBuilder formDataBuilder) {
		Buffer buffer = formDataBuilder.build();
		this.delegate
			.putHeader(HEADER_CONTENT_TYPE, headerFormDataWithBoundary(formDataBuilder.getBoundary()))
			.putHeader(HEADER_CONTENT_LENGTH, String.valueOf(buffer.length()))
			.setChunked(true)
			.write(buffer);
		return this;
	}

	private String headerFormDataWithBoundary(String boundary) {
		return String.format("%s; boundary=%s", CONTENT_FORM_DATA, boundary);
	}

	// =-=-=-=: DELEGATE :=-=-=-=

	@Override
	public HttpClientRequestTest exceptionHandler(Handler<Throwable> handler) {
		this.delegate.exceptionHandler(handler);
		return this;
	}

	@Override
	public HttpClientRequestTest write(Buffer data) {
		this.delegate.write(data);
		return this;
	}

	@Override
	public HttpClientRequestTest setWriteQueueMaxSize(int maxSize) {
		this.delegate.setWriteQueueMaxSize(maxSize);
		return this;
	}

	@Override
	public boolean writeQueueFull() {
		return this.delegate.writeQueueFull();
	}

	@Override
	public HttpClientRequestTest drainHandler(Handler<Void> handler) {
		this.delegate.drainHandler(handler);
		return this;
	}

	@Override
	public HttpClientRequestTest handler(Handler<HttpClientResponse> handler) {
		this.testResponseHandler = handler;
		return this;
	}

	@Override
	public HttpClientRequestTest pause() {
		this.delegate.pause();
		return this;
	}

	@Override
	public HttpClientRequestTest resume() {
		this.delegate.resume();
		return this;
	}

	@Override
	public HttpClientRequestTest endHandler(Handler<Void> endHandler) {
		this.delegate.endHandler(endHandler);
		return this;
	}

	@Override
	public HttpClientRequestTest setFollowRedirects(boolean followRedirects) {
		this.delegate.setFollowRedirects(followRedirects);
		return this;
	}

	@Override
	public HttpClientRequestTest setChunked(boolean chunked) {
		this.delegate.setChunked(chunked);
		return this;
	}

	@Override
	public boolean isChunked() {
		return this.delegate.isChunked();
	}

	@Override
	public HttpMethod method() {
		return this.delegate.method();
	}

	@Override
	public String getRawMethod() {
		return this.delegate.getRawMethod();
	}

	@Override
	public HttpClientRequestTest setRawMethod(String method) {
		this.delegate.setRawMethod(method);
		return this;
	}

	@Override
	public String absoluteURI() {
		return this.delegate.absoluteURI();
	}

	@Override
	public String uri() {
		return this.delegate.uri();
	}

	@Override
	public String path() {
		return this.delegate.path();
	}

	@Override
	public String query() {
		return this.delegate.query();
	}

	@Override
	public HttpClientRequestTest setHost(String host) {
		this.delegate.setHost(host);
		return this;
	}

	@Override
	public String getHost() {
		return this.delegate.getHost();
	}

	@Override
	public MultiMap headers() {
		return this.delegate.headers();
	}

	@Override
	public HttpClientRequestTest putHeader(String name, String value) {
		this.delegate.putHeader(name, value);
		return this;
	}

	@Override
	public HttpClientRequestTest putHeader(CharSequence name, CharSequence value) {
		this.delegate.putHeader(name, value);
		return this;
	}

	@Override
	public HttpClientRequestTest putHeader(String name, Iterable<String> values) {
		this.delegate.putHeader(name, values);
		return this;
	}

	@Override
	public HttpClientRequestTest putHeader(CharSequence name, Iterable<CharSequence> values) {
		this.delegate.putHeader(name, values);
		return this;
	}

	@Override
	public HttpClientRequestTest write(String chunk) {
		this.delegate.write(chunk);
		return this;
	}

	@Override
	public HttpClientRequestTest write(String chunk, String enc) {
		this.delegate.write(chunk, enc);
		return this;
	}

	@Override
	public HttpClientRequestTest continueHandler(Handler<Void> handler) {
		this.delegate.continueHandler(handler);
		return this;
	}

	@Override
	public HttpClientRequestTest sendHead() {
		this.delegate.sendHead();
		return this;
	}

	@Override
	public HttpClientRequestTest sendHead(Handler<HttpVersion> completionHandler) {
		this.delegate.sendHead(completionHandler);
		return this;
	}

	@Override
	public void end(String chunk) {
		this.delegate.end(chunk);
	}

	@Override
	public void end(String chunk, String enc) {
		this.delegate.end(chunk, enc);
	}

	@Override
	public void end(Buffer chunk) {
		this.delegate.end(chunk);
	}

	@Override
	public void end() {
		this.delegate.end();
	}

	@Override
	public HttpClientRequestTest setTimeout(long timeoutMs) {
		this.delegate.setTimeout(timeoutMs);
		return this;
	}

	@Override
	public HttpClientRequestTest pushHandler(Handler<HttpClientRequest> handler) {
		this.delegate.pushHandler(handler);
		return this;
	}

	@Override
	public boolean reset(long code) {
		return this.delegate.reset(code);
	}

	@Override
	public HttpConnection connection() {
		return this.delegate.connection();
	}

	@Override
	public HttpClientRequestTest connectionHandler(Handler<HttpConnection> handler) {
		this.delegate.connectionHandler(handler);
		return this;
	}

	@Override
	public HttpClientRequestTest writeCustomFrame(int type, int flags, Buffer payload) {
		this.delegate.writeCustomFrame(type, flags, payload);
		return this;
	}

}
