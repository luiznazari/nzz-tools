package br.com.senior.validation.exception;

import br.com.senior.validation.ValidationError;
import br.com.senior.validation.impl.ValidationErrorI18n;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

/**
 * @author Luiz.Nazari
 */
public class CustomValidationException extends RuntimeException {

	private static final long serialVersionUID = 3842185118616337611L;

	private final Set<ValidationError> errors;

	public CustomValidationException(String errorMessageCode) {
		this(new ValidationErrorI18n(errorMessageCode));
	}

	public CustomValidationException(ValidationError error) {
		this(Sets.newHashSet(error));
	}

	public CustomValidationException(Set<ValidationError> errors) {
		this.errors = Collections.unmodifiableSet(errors);
	}

	public Set<ValidationError> getErrors() {
		return this.errors;
	}
}
