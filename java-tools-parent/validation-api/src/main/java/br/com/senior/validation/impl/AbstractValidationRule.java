package br.com.senior.validation.impl;

import br.com.senior.validation.ValidationError;
import br.com.senior.validation.ValidationRule;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

/**
 * @param <T>
 * @author Luiz.Nazari
 */
public abstract class AbstractValidationRule<T> implements ValidationRule<T> {

	private final Set<ValidationError> validationErrors = Sets.newLinkedHashSet();

	protected void addError(ValidationError validationError) {
		if (validationError != null) {
			this.validationErrors.add(validationError);
		}
	}

	protected void addError(String errorMessageCode) {
		if (errorMessageCode != null) {
			this.validationErrors.add(new ValidationErrorI18n(errorMessageCode));
		}
	}

	protected Set<ValidationError> errors() {
		return Collections.unmodifiableSet(this.validationErrors);
	}

}
