package br.com.nzz.validation.impl;

import br.com.nzz.validation.ObjectValidator;
import br.com.nzz.validation.ValidationBuilder;
import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.ValidationRule;
import br.com.nzz.validation.di.DIContainer;
import br.com.nzz.validation.di.ValidationDIContainerManager;
import br.com.nzz.validation.exception.ValidationApiException;
import br.com.nzz.validation.impl.rules.BeanValidationRule;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @param <T> type of the builder's target object
 * @author Luiz.Nazari
 */
@Slf4j
public class ValidationBuilderImpl<T> implements ValidationBuilder<T> {

	private static final String TEMPLATE_ERROR_NO_PROVIDER_FOR_RULE = "Could not instantiate validation rule of class \"%s\". " +
		"Check if the class has an public constructor with zero arguments or is a injectable from any provided DI container.";

	private final ObjectValidatorImpl<T> validator;
	private final ValidationDIContainerManager diContainerManager;

	ValidationBuilderImpl() {
		this.validator = new ObjectValidatorImpl<>();
		this.diContainerManager = ValidationDIContainerManager.getInstance();
	}

	@Override
	public ValidationBuilder<T> withBeanValidation() {
		return this.with(BeanValidationRule.class);
	}

	@Override
	public ValidationBuilder<T> with(Class<? extends ValidationRule<? super T>> validationRuleClass) {
		DIContainer dependencyInjectionContainer = this.diContainerManager.getDIContainerFor(validationRuleClass);
		ValidationRule<? super T> providedValidationRule = dependencyInjectionContainer.provide(validationRuleClass);

		if (providedValidationRule == null) {
			String errorMessage = String.format(TEMPLATE_ERROR_NO_PROVIDER_FOR_RULE, validationRuleClass);
			throw new ValidationApiException(errorMessage);
		}

		return this.with(providedValidationRule);
	}


	@Override
	public ValidationBuilder<T> with(ValidationRule<? super T> validationRule) {
		this.validator.currentValidationBlock().add(validationRule);
		return this;
	}

	@Override
	public ValidationBuilder<T> with(Supplier<? extends ValidationError> validationErrorSupplier) {
		this.validator.currentValidationBlock().add(validationErrorSupplier);
		return this;
	}

	@Override
	public ValidationBuilder<T> blocking() {
		this.validator.finalizeCurrentValidationBlock();
		return this;
	}

	@Override
	public ObjectValidator<T> build() {
		return this.validator;
	}

}
