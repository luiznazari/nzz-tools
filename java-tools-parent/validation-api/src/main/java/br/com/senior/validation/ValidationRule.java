package br.com.senior.validation;

import br.com.senior.validation.exception.CustomValidationException;

import java.util.Set;

/**
 * A br.com.senior.validation rules to validate a given object.
 *
 * @param <T> the target object type
 * @author Luiz.Nazari
 */
public interface ValidationRule<T> {

	/**
	 * Validates the given object applying the br.com.senior.validation rules.<br>
	 * The rules can return a {@link Set} of br.com.senior.validation errors or throw a {@link CustomValidationException}.
	 *
	 * @param object the target object to be validated
	 * @return the br.com.senior.validation errors
	 * @throws CustomValidationException if an error is thrown
	 */
	Set<ValidationError> validate(T object);

}
