package br.com.senior.test;

import br.com.senior.test.http.FormDataBuilder;
import br.com.senior.test.http.HttpClientRequestTest;
import br.com.senior.test.http.UriBuilder;
import com.google.common.collect.Lists;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jspare.vertx.unit.ext.junit.VertxJspareUnitRunner;
import org.jspare.vertx.utils.JsonObjectLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for integration tests with Vert.x and Jspare.<br>
 * Provides shorthand methods for easing the writing testes, assertions, instantiate
 * common objects, improves test readability and provide Vert.x and Jspare objects such
 * as {@link Vertx} and {@link TestContext}.
 * <br><br>
 * Before the first test method, a Jspare context will be created based on a {@link Verticle},
 * all the route mappings, beans, injectors, etc. will be available for use. It also provides
 * support for {@link javax.inject.Inject @Inject} and {@link org.jspare.jpa.annotation.RepositoryInject @RepositoryInject}.
 * <br><br>
 * IMPORTANT: in this version, due to the lack of a proper test data base, the persistence layer
 * is mocked. All injected repositories will be mocks (as well as injections inside services and
 * components) and the same instances will be available in test methods.
 *
 * @author Luiz.Nazari
 */
@Slf4j
@RunWith(VertxJspareUnitRunner.class)
public abstract class IntegrationTestJspare extends UnitTestSpring {

	private static final String NULL_STRING = "null";
	private static final int MAX_TEST_TIMEOUT_MILLISECONDS = 5000;

	private final TestVerticle testVerticleAnnotation;

	protected static final Vertx vertx;
	private static final AtomicBoolean initialized;
	protected TestContext context;

	static {
		vertx = Vertx.vertx();
		initialized = new AtomicBoolean(false);
	}

	protected IntegrationTestJspare() {
		this.testVerticleAnnotation = this.getClass().getAnnotation(TestVerticle.class);
		if (this.testVerticleAnnotation == null) {
			throw new AssertionError("The test class must be annotated with @TestVerticle.");
		}
	}

	@Before
	public final void setUpJspare(TestContext context) {
		runBeforeOnlyOnce(() -> {
			JsonObject configOptions = loadConfigOptionsJson();
			DeploymentOptions options = new DeploymentOptions().setConfig(configOptions);
			vertx.deployVerticle(testVerticleAnnotation.value().getName(), options, context.asyncAssertSuccess());

			JspareIntegrationTestDIManager.registerMockInjectStrategy();
		});
	}

	protected void runBeforeOnlyOnce(Runnable runnable) {
		if (!initialized.get()) {
			runnable.run();
		}
	}

	private JsonObject loadConfigOptionsJson() {
		JsonObjectLoader jsonObjectLoader = new JsonObjectLoader();
		return jsonObjectLoader.loadOptions(StringUtils.EMPTY, testVerticleAnnotation.configFileDir());
	}

	@Before
	public final void initTestContext(TestContext context) {
		this.context = context;
		// Contorno temporário enquanto não houver banco de dados para testes.
		JspareIntegrationTestDIManager.initializeMocks(this);
	}

	@After
	public final void resetMocks() {
		if (!initialized.get()) {
			initialized.set(true);
		}

		// Contorno temporário enquanto não houver banco de dados para testes.
		JspareIntegrationTestDIManager.resetMockedInstances();
	}

	// =-=-=-=-=: Builders :=-=-=-=-=

	protected JsonObject json() {
		return new JsonObject();
	}

	protected UriBuilder uri(String uri) {
		return new UriBuilder(uri);
	}

	protected FormDataBuilder formData() {
		return new FormDataBuilder();
	}

	// =-=-=-=-=: Request :=-=-=-=-=

	protected HttpClientRequestTest get(UriBuilder uriBuilder, Handler<HttpClientResponse> responseHandler) {
		return this.get(uriBuilder.build(), responseHandler);
	}

	protected HttpClientRequestTest get(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return this.get(requestURI).handler(responseHandler);
	}

	protected HttpClientRequestTest post(UriBuilder uriBuilder, Handler<HttpClientResponse> responseHandler) {
		return this.post(uriBuilder.build(), responseHandler);
	}

	protected HttpClientRequestTest post(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return this.post(requestURI).handler(responseHandler);
	}

	protected HttpClientRequestTest put(UriBuilder uriBuilder, Handler<HttpClientResponse> responseHandler) {
		return this.put(uriBuilder.build(), responseHandler);
	}

	protected HttpClientRequestTest put(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return this.put(requestURI).handler(responseHandler);
	}

	protected HttpClientRequestTest delete(UriBuilder uriBuilder, Handler<HttpClientResponse> responseHandler) {
		return this.delete(uriBuilder.build(), responseHandler);
	}

	protected HttpClientRequestTest delete(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return this.delete(requestURI).handler(responseHandler);
	}

	protected HttpClientRequestTest get(UriBuilder uriBuilder) {
		return this.get(uriBuilder.build());
	}

	protected HttpClientRequestTest get(String requestURI) {
		return this.request(HttpMethod.GET, requestURI);
	}

	protected HttpClientRequestTest post(UriBuilder uriBuilder) {
		return this.post(uriBuilder.build());
	}

	protected HttpClientRequestTest post(String requestURI) {
		return this.request(HttpMethod.POST, requestURI);
	}

	protected HttpClientRequestTest put(UriBuilder uriBuilder) {
		return this.put(uriBuilder.build());
	}

	protected HttpClientRequestTest put(String requestURI) {
		return this.request(HttpMethod.PUT, requestURI);
	}

	protected HttpClientRequestTest delete(UriBuilder uriBuilder) {
		return this.delete(uriBuilder.build());
	}

	protected HttpClientRequestTest delete(String requestURI) {
		return this.request(HttpMethod.DELETE, requestURI);
	}

	protected HttpClientRequestTest request(HttpMethod httpMethod, String requestURI) {
		Async async = context.async(); // NOSONAR

		HttpClientRequest httpClientRequest = vertx.createHttpClient()
			.request(httpMethod, testVerticleAnnotation.port(), testVerticleAnnotation.host(), requestURI);

		httpClientRequest
			.setTimeout(MAX_TEST_TIMEOUT_MILLISECONDS)
			.exceptionHandler(requestExceptionHandler(httpClientRequest, context));

		return HttpClientRequestTest.of(httpClientRequest)
			.handlerWrapper((response, originalResponseHandler) -> {
					response.exceptionHandler(context::fail);
					originalResponseHandler.handle(response);
					async.complete();
				}
			);
	}

	private Handler<Throwable> requestExceptionHandler(HttpClientRequest httpClientRequest, TestContext context) {
		return throwable -> {
			log.error("Error requesting [{}] {}", httpClientRequest.method(), httpClientRequest.absoluteURI());

			if (throwable instanceof TimeoutException) {
				log.error("You've got a TimeoutException! Did you call '.end()' after declaring the HTTP request?");
			}

			context.fail(throwable);
		};
	}

	// =-=-=-=-=: Helpers and utility methods :=-=-=-=-=

	@SuppressWarnings({"unchecked", "unused"})
	protected <T> Page<T> getPageFromBody(Buffer bodyBuffer, Class<T> clazz) {
		if (bodyBuffer == null) {
			return new PageImpl<>(Collections.emptyList());
		}

		JsonObject pageJson = bodyBuffer.toJsonObject();
		JsonArray contentArray = pageJson.getJsonArray("content");
		List<T> list = getListFromBody(contentArray.encode(), clazz);

		return new PageImpl<>(list);
	}

	protected <T> List<T> getListFromBody(Buffer bodyBuffer, Class<T> clazz) {
		if (bodyBuffer == null) {
			return Collections.emptyList();
		}
		return this.getListFromBody(bodyBuffer.toString(), clazz);
	}

	@SuppressWarnings("unchecked")
	protected <T> T[] getArrayFromBody(Buffer bodyBuffer, Class<T> clazz) {
		if (bodyBuffer == null) {
			return (T[]) Array.newInstance(clazz, 0);
		}

		return this.getArrayFromBody(bodyBuffer.toString(), clazz);
	}

	protected <T> List<T> getListFromBody(String jsonArray, Class<T> clazz) {
		return Lists.newArrayList(getArrayFromBody(jsonArray, clazz));
	}

	@SuppressWarnings("unchecked")
	protected <T> T[] getArrayFromBody(String jsonArray, Class<T> clazz) {
		T[] typedEmptyArray = (T[]) Array.newInstance(clazz, 0);
		if (jsonArray == null) {
			return typedEmptyArray;
		}

		Class<? extends T[]> classArray = (Class<? extends T[]>) typedEmptyArray.getClass();
		return Json.decodeValue(jsonArray, classArray);
	}

	// =-=-=-=-=: Asserts :=-=-=-=-=

	protected void isOk(HttpClientResponse response) {
		assertStatusCode(HttpResponseStatus.OK.code(), response);
	}

	protected void isBadRequest(HttpClientResponse response) {
		assertStatusCode(HttpResponseStatus.BAD_REQUEST.code(), response);
	}

	protected void isNotFound(HttpClientResponse response) {
		assertStatusCode(HttpResponseStatus.NOT_FOUND.code(), response);
	}

	protected void isForbidden(HttpClientResponse response) {
		assertStatusCode(HttpResponseStatus.FORBIDDEN.code(), response);
	}

	protected void assertStatusCode(int expectedStatusCode, HttpClientResponse response) {
		context.assertEquals(expectedStatusCode, response.statusCode(), "Unexpected HTTP Status Code");
	}

	protected void isEmptyBody(HttpClientResponse response) {
		response.bodyHandler(bodyBuffer -> {
			String body = bodyBuffer.toString();
			boolean isNullString = NULL_STRING.equals(body);
			boolean isEmpty = body.isEmpty();
			context.assertTrue(isNullString || isEmpty, "Expected an empty response's body.");
		});
	}

}
