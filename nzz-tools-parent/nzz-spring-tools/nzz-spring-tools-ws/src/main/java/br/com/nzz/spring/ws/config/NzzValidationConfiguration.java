package br.com.nzz.spring.ws.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.nzz.validation.CustomValidator;
import br.com.nzz.validation.impl.CustomValidatorImpl;

@Configuration
@ConditionalOnClass(CustomValidator.class)
public class NzzValidationConfiguration {

	@Bean
	public CustomValidator customValidatorBean() {
		return new CustomValidatorImpl();
	}

}
