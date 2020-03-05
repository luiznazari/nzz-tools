package br.com.nzz.validation;

import br.com.nzz.validation.exception.CustomValidationException;

import java.util.Set;

public class TestValidationRule implements ValidationRule<BeautifulObject> {

	private static final String ERROR = "test.random.error";

	@Override
	public Set<ValidationError> validate(BeautifulObject object) {
		throw new CustomValidationException(ERROR);
	}

}