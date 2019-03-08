package br.com.senior.validation.impl.rules;

import br.com.senior.validation.ValidationError;
import br.com.senior.validation.impl.AbstractValidationRule;

import java.util.Set;

public class NotNullValidationRule extends AbstractValidationRule<Object> {

	public static final String ERROR = "javax.validation.constraints.NotNull.message";

	@Override
	public Set<ValidationError> validate(Object object) {
		if (object == null) {
			super.addError(ERROR);
		}
		return super.errors();
	}

}