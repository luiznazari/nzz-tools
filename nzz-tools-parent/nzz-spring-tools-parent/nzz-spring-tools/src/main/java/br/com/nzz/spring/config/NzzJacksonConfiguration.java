package br.com.nzz.spring.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.nzz.spring.adapter.JacksonLoader;

/**
 * Spring Boot configuration class to configure all ObjectMapper with nzz's serializers.
 * <br>
 * Usage:
 * <pre>
 * {@code @Configuration}
 * {@code @Import(NzzJacksonConfiguration.class)}
 * public class AnySpringBootConfigurationClass {
 *     ...
 * }
 * </pre>
 */
@Configuration
@Import(JacksonAutoConfiguration.class)
public class NzzJacksonConfiguration {

	@Bean
	public Module defaultNzzJacksonModules() {
		SimpleModule serializersModule = new SimpleModule();
		JacksonLoader.loadSerializersFromClassPath().forEach(serializersModule::addSerializer);
		return serializersModule;
	}

}