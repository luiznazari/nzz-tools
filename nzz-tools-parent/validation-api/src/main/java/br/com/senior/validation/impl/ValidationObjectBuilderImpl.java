package br.com.senior.validation.impl;

import br.com.senior.validation.ValidationError;
import br.com.senior.validation.ValidationObjectBuilder;
import br.com.senior.validation.ValidationResult;
import br.com.senior.validation.ValidationRule;

import java.util.function.Supplier;

/**
 * @param <T>
 * @author Luiz.Nazari
 */
public class ValidationObjectBuilderImpl<T> extends ValidationBuilderImpl<T> implements ValidationObjectBuilder<T> {

	private final T target;

	ValidationObjectBuilderImpl(T target) {
		this.target = target;
	}

	@Override
	public ValidationResult validate() {
		return this.build().validate(this.target);
	}

	@Override
	public ValidationObjectBuilder<T> withBeanValidation() {
		super.withBeanValidation();
		return this;
	}

	@Override
	public ValidationObjectBuilder<T> with(ValidationRule<? super T> validationRule) {
		super.with(validationRule);
		return this;
	}

	@Override
	public ValidationObjectBuilder<T> with(Class<? extends ValidationRule<? super T>> validationRuleClass) {
		super.with(validationRuleClass);
		return this;
	}

	@Override
	public ValidationObjectBuilder<T> with(Supplier<? extends ValidationError> validationErrorSupplier) {
		super.with(validationErrorSupplier);
		return this;
	}

	@Override
	public ValidationObjectBuilder<T> blocking() {
		super.blocking();
		return this;
	}

}
