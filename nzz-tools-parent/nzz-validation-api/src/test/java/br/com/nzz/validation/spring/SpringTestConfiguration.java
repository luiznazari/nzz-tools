package br.com.nzz.validation.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.nzz.validation.CustomValidator;
import br.com.nzz.validation.TestIdNotNullValidator;
import br.com.nzz.validation.TestValidationRule;
import br.com.nzz.validation.impl.CustomValidatorImpl;

@Configuration
public class SpringTestConfiguration {

	@Bean
	public CustomValidator customValidatorBean() {
		return new CustomValidatorImpl();
	}

	@Bean
	public TestValidationRule beanTestValidationRule() {
		return new TestValidationRule();
	}

	@Bean
	public TestIdNotNullValidator beanTestIdNotNullValidator() {
		return new TestIdNotNullValidator();
	}

}
