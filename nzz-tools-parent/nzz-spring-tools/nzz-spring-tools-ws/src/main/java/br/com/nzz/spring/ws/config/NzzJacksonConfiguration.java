package br.com.nzz.spring.ws.config;

import br.com.nzz.spring.ws.adapter.JacksonLoader;
import br.com.nzz.spring.ws.adapter.XMLGregorianCalendarJsonSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;

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