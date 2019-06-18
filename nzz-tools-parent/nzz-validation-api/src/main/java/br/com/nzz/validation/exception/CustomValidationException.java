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

	private static final long serialVersionUID = 3054429759853682467L;
	private static final String TEMPLATE_MESSAGE = "%s errors found.";

	@Getter
	private final Set<ValidationError> errors;

	public CustomValidationException(String errorMessageCode) {
		this(new I18nValidationError(errorMessageCode));
	}

	public CustomValidationException(ValidationError error) {
		this(Sets.newHashSet(error));
	}

	public CustomValidationException(Set<? extends ValidationError> errors) {
		super(String.format(TEMPLATE_MESSAGE, errors.size()));
		this.errors = Collections.unmodifiableSet(errors);
	}

}
