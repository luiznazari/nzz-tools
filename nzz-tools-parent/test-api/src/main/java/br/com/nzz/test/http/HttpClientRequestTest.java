package br.com.nzz.test.http;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a client-side HTTP request.<br>
 * Provide shorthand methods for easing common usage of {@link HttpClientRequest}.
 *
 * @author Luiz.Nazari
 * @see HttpClientRequest
 */
public interface HttpClientRequestTest extends HttpClientRequest {

	HttpClientRequestTest handlerWrapper(BiConsumer<HttpClientResponse, Handler<HttpClientResponse>> responseHandlerConsumer);

	/**
	 * Configure the request for requesting json data, by adding {@code content-type} and
	 * {@code content-length} headers and adding the body as a json-formatted string.
	 *
	 * @param object - the object to be converted to json.
	 * @return this
	 */
	HttpClientRequestTest jsonOf(Object object);

	/**
	 * Configure the request for requesting json data, by adding {@code content-type} and
	 * {@code content-length} headers and adding the body as a json-formatted string.
	 *
	 * @param jsonObject - the json object used as request body.
	 * @return this
	 */
	HttpClientRequestTest json(JsonObject jsonObject);

	/**
	 * Configure the request for requesting json data, by adding {@code content-type} and
	 * {@code content-length} headers and adding the body as a json-formatted string.<br>
	 * <br>
	 * Request using a simple one-property json.
	 *
	 * @param property - the json property name.
	 * @param value    - the json property value.
	 * @return this
	 */
	HttpClientRequestTest json(String property, Object value);

	HttpClientRequestTest formData(FormDataBuilder formDataBuilder);

	HttpClientRequestTest formData(Consumer<FormDataBuilder> formDataBuilderConsumer);

	/**
	 * Provides an implementation of {@link HttpClientRequestTest}.
	 *
	 * @param httpClientRequest - the http client request used to delegate method calls.
	 * @return a new instance of {@link HttpClientRequestTest}.
	 */
	static HttpClientRequestTest of(HttpClientRequest httpClientRequest) {
		return new HttpClientRequestTestImpl(httpClientRequest);
	}

	// =-=-=-=: OVERRIDES :=-=-=-=

	@Override
	HttpClientRequestTest handler(Handler<HttpClientResponse> handler);

}
