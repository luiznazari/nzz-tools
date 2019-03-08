package br.com.senior.validation;

import br.com.senior.validation.exception.CustomValidationException;
import br.com.senior.validation.impl.ValidationResultImpl;
import com.google.common.collect.Sets;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A br.com.senior.validation result to manage br.com.senior.validation errors and define method callbacks to fluently handle
 * br.com.senior.validation errors.
 *
 * @author Luiz.Nazari
 */
public interface ValidationResult {

	/**
	 * Defines a consumer to be called if the br.com.senior.validation result has br.com.senior.validation errors.
	 *
	 * @param consumer the error consumer
	 * @return this
	 */
	ValidationResult onError(Consumer<Set<? extends ValidationError>> consumer);

	/**
	 * Defines an error callback, if the br.com.senior.validation result has br.com.senior.validation errors, an {@link CustomValidationException}
	 * containing all br.com.senior.validation errors will be thrown. The errors can be serialized.
	 *
	 * @return this
	 * @throws CustomValidationException if the result contains br.com.senior.validation errors
	 */
	ValidationResult onErrorThrowException();

	/**
	 * Defines an callback to be called if the br.com.senior.validation result does not contains br.com.senior.validation errors.
	 *
	 * @param runnable the success callback
	 * @return this
	 */
	ValidationResult ifNoError(Runnable runnable);

	/**
	 * @return an {@link java.util.Collections.UnmodifiableSet} containing all br.com.senior.validation errors
	 */
	Set<ValidationError> getErrors();

	/**
	 * @return {@code true} case the br.com.senior.validation result contains br.com.senior.validation errors, {@code false} otherwise
	 */
	boolean hasErrors();

	/**
	 * Chains an error supplier to the br.com.senior.validation result. The errors returned or thrown by the supplier
	 * will be added to the br.com.senior.validation result's errors.<br>
	 * If the supplier returns an error or throws a {@link CustomValidationException}, the error(s) will
	 * be added to the result, if it returns {@code null} or does not throws an exception, no errors will be
	 * added.
	 *
	 * @param errorSupplier the error supplier
	 * @return this
	 */
	ValidationResult validate(Supplier<? extends ValidationError> errorSupplier);

	/**
	 * Returns a new {@link ValidationResult} instance containing the given constraint validations errors.
	 *
	 * @param constraintViolations the result's errors
	 * @return the br.com.senior.validation result
	 */
	static ValidationResult of(Set<? extends ConstraintViolation<?>> constraintViolations) {
		return new ValidationResultImpl(constraintViolations, Collections.emptySet());
	}

	/**
	 * Returns a new {@link ValidationResult} instance containing no errors.
	 *
	 * @return the empty br.com.senior.validation result
	 */
	static ValidationResult empty() {
		return ofErrors(Collections.emptySet());
	}

	/**
	 * Returns a new {@link ValidationResult} instance containing the given error.
	 *
	 * @param error the result's error
	 * @return the br.com.senior.validation result
	 */
	static ValidationResult ofError(ValidationError error) {
		return ofErrors(Sets.newHashSet(error));
	}

	/**
	 * Returns a new {@link ValidationResult} instance containing the given validations errors.
	 *
	 * @param errors the result's errors
	 * @return the br.com.senior.validation result
	 */
	static ValidationResult ofErrors(Set<? extends ValidationError> errors) {
		return new ValidationResultImpl(Collections.emptySet(), errors);
	}

}
