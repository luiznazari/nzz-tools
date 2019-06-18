package br.com.nzz.spring.ws.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring Boot configuration class to configure nzz's beans.
 * <br>
 * Usage:
 * <pre>
 * {@code @Configuration}
 * {@code @Import(NzzConfiguration.class)}
 * public class AnySpringBootConfigurationClass {
 *     ...
 * }
 * </pre>
 */
@Configuration
@ComponentScan({"br.com.nzz.spring.ws.controller"})
@Import({
	NzzValidationConfiguration.class,
	NzzJacksonConfiguration.class
})
public class NzzConfiguration {

}