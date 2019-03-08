package br.com.senior.validation;

import br.com.senior.validation.exception.CustomValidationException;

import java.util.Collections;
import java.util.Set;

public class TestIdNotNullValidator implements ValidationRule<BeautifulId> {

	public static final String ERROR = "test.error.entity.id.null";

	@Override
	public Set<ValidationError> validate(BeautifulId beautifulId) {
		if (beautifulId.getId() == null) {
			throw new CustomValidationException(ERROR);
		}
		return Collections.emptySet();
	}

}