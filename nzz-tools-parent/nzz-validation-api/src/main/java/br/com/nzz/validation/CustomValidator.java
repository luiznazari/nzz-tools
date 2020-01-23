package br.com.nzz.validation;

import java.util.function.Supplier;

import br.com.nzz.validation.exception.CustomValidationException;

/**
 * The custom validator allows fluent br.com.nzz.validation, chaining br.com.nzz.validation rules, managing
 * br.com.nzz.validation rules execution and defining callback functions to handle br.com.nzz.validation errors.
 *
 * @author Luiz.Nazari
 */
public interface CustomValidator {

	/**
	 * Validates an object applying bean validation.
	 *
	 * @param object the object to be validated
	 * @return the br.com.nzz.validation result
	 */
	ValidationResult validate(Object object);

	/**
	 * Validates an object with the given validation class. The br.com.nzz.validation rule should
	 * be provided by a known Dependency Injection container.<br>
	 * This method is a simple form of:
	 * <pre>{@code
	 * 	validator.builder(object).with(validationRuleClass).validate();
	 * }</pre>
	 *
	 * @param object              the object to be validated
	 * @param validationRuleClass the validation rule class
	 * @return the br.com.nzz.validation result
	 */
	<T> ValidationResult validate(T object, Class<? extends ValidationRule<? super T>> validationRuleClass);

	/**
	 * Validates an error supplier. If it returns an error or throws a {@link CustomValidationException},
	 * the result will contains errors. If it returns {@code null} or does not throws an exception, no
	 * errors will be reported.
	 *
	 * @param errorSupplier the error supplier
	 * @return the br.com.nzz.validation result
	 */
	ValidationResult validate(Supplier<ValidationError> errorSupplier);

	/**
	 * Returns a new br.com.nzz.validation builder for chaining br.com.nzz.validation rules, allowing blocking validations.
	 *
	 * @param objectClass target instance type
	 * @param <T>         the builder's br.com.nzz.validation target
	 * @return a new br.com.nzz.validation builder
	 */
	<T> ValidationBuilder<T> builder(Class<T> objectClass);

	/**
	 * Returns a new br.com.nzz.validation builder for chaining br.com.nzz.validation rules, allowing blocking validations. <br>
	 * The {@link ValidationObjectBuilder} already knows the target object to be validated, therefore,
	 * it does not requires to be informed later, allowing a direct call to {@link ValidationObjectBuilder#validate()}.
	 *
	 * @param object target instance, object to be validated
	 * @param <T>    the builder's br.com.nzz.validation target
	 * @return a new br.com.nzz.validation builder with the target instance
	 */
	<T> ValidationObjectBuilder<T> builder(T object);

}
