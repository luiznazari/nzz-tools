package br.com.senior.validation.impl;

import br.com.senior.validation.exception.CustomValidationException;
import br.com.senior.validation.ValidationError;
import br.com.senior.validation.ValidationRule;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @param <T>
 * @author Luiz.Nazari
 */
class ValidationBlock<T> {

	private final Set<ValidationRule<? super T>> validationRules = Sets.newLinkedHashSet();

	void add(ValidationRule<? super T> validationRule) {
		this.validationRules.add(validationRule);
	}

	void add(Supplier<? extends ValidationError> validationErrorSupplier) {
		this.validationRules.add(new ValidationRuleErrorSupplier(validationErrorSupplier));
	}

	Set<ValidationError> execute(T target) {
		Set<ValidationError> errors = Sets.newLinkedHashSet();

		this.validationRules.forEach(validationRule -> {
			try {
				Set<ValidationError> validationErrors = validationRule.validate(target);
				if (validationErrors != null) {
					errors.addAll(validationErrors);
				}
			} catch (CustomValidationException e) {
				errors.addAll(e.getErrors());
			}
		});

		return Collections.unmodifiableSet(errors);
	}

	@AllArgsConstructor
	private class ValidationRuleErrorSupplier implements ValidationRule<Object> {

		private final Supplier<? extends ValidationError> supplier;

		@Override
		public Set<ValidationError> validate(Object object) {
			ValidationError validationError = this.supplier.get();
			if (validationError != null) {
				return Sets.newHashSet(validationError);
			}
			return Collections.emptySet();
		}
	}


}
