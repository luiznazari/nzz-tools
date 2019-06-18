package br.com.nzz.validation.constraint;

import br.com.nzz.validation.BeautifulId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Default implementation of the {@link BeautifulIdNotNull} br.com.nzz.validation constraint.
 *
 * @author Luiz Felipe Nazari
 */
public class BeautifulIdNotNullValidator implements ConstraintValidator<BeautifulIdNotNull, BeautifulId> {

	@Override
	public void initialize(BeautifulIdNotNull constraintAnnotation) {
		// Nothing to do here.
	}

	@Override
	public boolean isValid(BeautifulId beautifulId, ConstraintValidatorContext context) {
		return beautifulId != null && beautifulId.getId() != null;
	}

}

