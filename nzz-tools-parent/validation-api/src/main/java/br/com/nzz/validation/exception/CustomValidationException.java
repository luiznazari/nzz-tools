package br.com.nzz.validation.exception;

import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.impl.I18nValidationError;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

/**
 * @author Luiz.Nazari
 */
public class CustomValidationException extends ErrorMessageException {

	private static final long serialVersionUID = 8128285642545447027L;

	@Getter
	private final Set<ValidationError> errors;

	public CustomValidationException(String errorMessageCode) {
		this(new I18nValidationError(errorMessageCode));
	}

	public CustomValidationException(ValidationError error) {
		this(Sets.newHashSet(error));
	}

	public CustomValidationException(Set<? extends ValidationError> errors) {
		super();
		this.errors = Collections.unmodifiableSet(errors);
	}

}
