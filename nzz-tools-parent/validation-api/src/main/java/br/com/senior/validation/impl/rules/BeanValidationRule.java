package br.com.senior.validation.impl.rules;

import br.com.senior.validation.ValidationError;
import br.com.senior.validation.ValidationRule;
import br.com.senior.validation.impl.BeanValidator;
import br.com.senior.validation.impl.ConstraintViolationError;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Luiz.Nazari
 */
public class BeanValidationRule implements ValidationRule<Object> {

	private BeanValidator validator;

	public BeanValidationRule() {
		this.validator = new BeanValidator();
	}

	@Override
	public Set<ValidationError> validate(Object object) {
		Set<ConstraintViolation<Object>> constraintViolations = this.validator.validate(object);
		return constraintViolations.stream().map(ConstraintViolationError::new).collect(Collectors.toSet());
	}

}
