package br.com.nzz.validation;

import br.com.nzz.validation.exception.CustomValidationException;

import java.util.Set;

/**
 * A br.com.nzz.validation rules to validate a given object.
 *
 * @param <T> the target object type
 * @author Luiz.Nazari
 */
public interface ValidationRule<T> {

	/**
	 * Validates the given object applying the br.com.nzz.validation rules.<br>
	 * The rules can return a {@link Set} of br.com.nzz.validation errors or throw a {@link CustomValidationException}.
	 *
	 * @param object the target object to be validated
	 * @return the br.com.nzz.validation errors
	 * @throws CustomValidationException if an error is thrown
	 */
	Set<ValidationError> validate(T object);

}
