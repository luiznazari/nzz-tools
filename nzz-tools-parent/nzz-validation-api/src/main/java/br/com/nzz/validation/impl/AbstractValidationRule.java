package br.com.nzz.validation.impl;

import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.ValidationRule;
import com.google.common.collect.Sets;
import lombok.NonNull;

import java.util.Collections;
import java.util.Set;

/**
 * @param <T> type of the validation rule.
 * @author Luiz.Nazari
 */
public abstract class AbstractValidationRule<T> implements ValidationRule<T> {

	private final Set<ValidationError> validationErrors = Sets.newLinkedHashSet();

	protected void addError(ValidationError validationError) {
		if (validationError != null) {
			this.validationErrors.add(validationError);
		}
	}

	protected I18nValidationError addError(@NonNull String errorMessageCode) {
		I18nValidationError validationErrorI18n = new I18nValidationError(errorMessageCode);
		this.validationErrors.add(validationErrorI18n);
		return validationErrorI18n;
	}

	protected Set<ValidationError> errors() {
		return Collections.unmodifiableSet(this.validationErrors);
	}

}
