package br.com.senior.validation;

import br.com.senior.validation.exception.CustomValidationException;

import java.util.function.Supplier;

/**
 * The custom validator allows fluent br.com.senior.validation, chaining br.com.senior.validation rules, managing
 * br.com.senior.validation rules execution and defining callback functions to handle br.com.senior.validation errors.
 *
 * @author Luiz.Nazari
 */
public interface CustomValidator {

	/**
	 * Validates an object applying bean validation.
	 *
	 * @param object the object to be validated
	 * @return the br.com.senior.validation result
	 */
	ValidationResult validate(Object object);

	/**
	 * Validates an error supplier. If it returns an error or throws a {@link CustomValidationException},
	 * the result will contains errors. If it returns {@code null} or does not throws an exception, no
	 * errors will be reported.
	 *
	 * @param errorSupplier the error supplier
	 * @return the br.com.senior.validation result
	 */
	ValidationResult validate(Supplier<ValidationError> errorSupplier);

	/**
	 * Returns a new br.com.senior.validation builder for chaining br.com.senior.validation rules, allowing blocking validations.
	 *
	 * @param objectClass target instance type
	 * @param <T>         the builder's br.com.senior.validation target
	 * @return a new br.com.senior.validation builder
	 */
	<T> ValidationBuilder<T> builder(Class<T> objectClass);

	/**
	 * Returns a new br.com.senior.validation builder for chaining br.com.senior.validation rules, allowing blocking validations. <br>
	 * The {@link ValidationObjectBuilder} already knows the target object to be validated, therefore,
	 * it does not requires to be informed later, allowing a direct call to {@link ValidationObjectBuilder#validate()}.
	 *
	 * @param object target instance, object to be validated
	 * @param <T>    the builder's br.com.senior.validation target
	 * @return a new br.com.senior.validation builder with the target instance
	 */
	<T> ValidationObjectBuilder<T> builder(T object);

}
