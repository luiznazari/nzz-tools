package br.com.nzz.validation;

/**
 * An object validator with custom br.com.nzz.validation rules.
 *
 * @param <T> the target object type
 * @author Luiz.Nazari
 */
public interface ObjectValidator<T> {

	/**
	 * Validates the given object.<br>
	 *
	 * @param object the target object to be validated
	 * @return the br.com.nzz.validation result
	 */
	ValidationResult validate(T object);

}
