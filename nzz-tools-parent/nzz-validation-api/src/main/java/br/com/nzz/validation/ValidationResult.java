package br.com.nzz.validation;

import br.com.nzz.validation.exception.CustomValidationException;
import br.com.nzz.validation.impl.ValidationResultImpl;
import com.google.common.collect.Sets;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A br.com.nzz.validation result to manage br.com.nzz.validation errors and define method callbacks to fluently handle
 * br.com.nzz.validation errors.
 *
 * @author Luiz.Nazari
 */
public interface ValidationResult {

	/**
	 * Defines a consumer to be called if the br.com.nzz.validation result has br.com.nzz.validation errors.
	 *
	 * @param consumer the error consumer
	 * @return this
	 */
	ValidationResult onError(Consumer<Set<? extends ValidationError>> consumer);

	/**
	 * Defines an error callback, if the br.com.nzz.validation result has br.com.nzz.validation errors, an {@link CustomValidationException}
	 * containing all br.com.nzz.validation errors will be thrown. The errors can be serialized.
	 *
	 * @return this
	 * @throws CustomValidationException if the result contains br.com.nzz.validation errors
	 */
	ValidationResult onErrorThrowException();

	/**
	 * Defines an error callback, if the br.com.nzz.validation result has br.com.nzz.validation errors, an exception
	 * containing all br.com.nzz.validation errors will be thrown. The errors can be serialized.
	 *
	 * @return this
	 * @param throwableSupplier the function supplier
	 */
	<X extends Throwable> ValidationResult onErrorThrow(Supplier<X> throwableSupplier) throws X;

	/**
	 * Defines an error callback, if the br.com.nzz.validation result has br.com.nzz.validation errors, an exception
	 * containing all br.com.nzz.validation errors will be thrown. The errors can be serialized.
	 *
	 * @return this
	 * @param throwableFunction the function supplier
	 */
	<X extends Throwable> ValidationResult onErrorThrow(Function<Set<? extends ValidationError>, X> throwableFunction) throws X;

	/**
	 * Defines an callback to be called if the br.com.nzz.validation result does not contains br.com.nzz.validation errors.
	 *
	 * @param runnable the success callback
	 * @return this
	 */
	ValidationResult ifNoError(Runnable runnable);

	/**
	 * @return an {@link java.util.Set} containing all br.com.nzz.validation errors
	 */
	Set<ValidationError> getErrors();

	/**
	 * @return {@code true} case the br.com.nzz.validation result contains br.com.nzz.validation errors, {@code false} otherwise
	 */
	boolean hasErrors();

	/**
	 * Chains an error supplier to the br.com.nzz.validation result. The errors returned or thrown by the supplier
	 * will be added to the br.com.nzz.validation result's errors.<br>
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
	 * @return the br.com.nzz.validation result
	 */
	static ValidationResult of(Set<? extends ConstraintViolation<?>> constraintViolations) {
		return new ValidationResultImpl(constraintViolations, Collections.emptySet());
	}

	/**
	 * Returns a new {@link ValidationResult} instance containing no errors.
	 *
	 * @return the empty br.com.nzz.validation result
	 */
	static ValidationResult empty() {
		return ofErrors(Collections.emptySet());
	}

	/**
	 * Returns a new {@link ValidationResult} instance containing the given error.
	 *
	 * @param error the result's error
	 * @return the br.com.nzz.validation result
	 */
	static ValidationResult ofError(ValidationError error) {
		return ofErrors(Sets.newHashSet(error));
	}

	/**
	 * Returns a new {@link ValidationResult} instance containing the given validations errors.
	 *
	 * @param errors the result's errors
	 * @return the br.com.nzz.validation result
	 */
	static ValidationResult ofErrors(Set<? extends ValidationError> errors) {
		return new ValidationResultImpl(Collections.emptySet(), errors);
	}

}
