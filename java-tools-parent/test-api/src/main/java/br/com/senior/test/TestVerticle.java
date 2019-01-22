package br.com.senior.test;

import io.vertx.core.Verticle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to inform the which {@link Verticle} class will be used when running the integration tests. <br>
 * It'll define routes, handlers and custom Vert.x configurations.
 *
 * @author Luiz.Nazari
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TestVerticle {

	/**
	 * @return a Verticle class which context will be used in integration tests.
	 */
	Class<? extends Verticle> value();

	/**
	 * @return the configuration file directory.
	 */
	String configFileDir() default "src/test/resources/dev_test.json";

	int port() default 9999;

	String host() default "localhost";

}
