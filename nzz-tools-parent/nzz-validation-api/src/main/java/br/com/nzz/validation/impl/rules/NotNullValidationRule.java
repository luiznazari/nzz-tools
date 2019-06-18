package br.com.nzz.validation.impl.rules;

import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.impl.AbstractValidationRule;

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