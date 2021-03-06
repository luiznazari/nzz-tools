package br.com.nzz.validation.impl;

import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.ValidationObjectBuilder;
import br.com.nzz.validation.ValidationResult;
import br.com.nzz.validation.ValidationRule;

import java.util.function.Supplier;

/**
 * @param <T> type of the builder's target object
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
