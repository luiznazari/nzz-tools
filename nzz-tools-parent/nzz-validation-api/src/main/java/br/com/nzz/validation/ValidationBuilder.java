package br.com.nzz.validation;

import java.util.function.Supplier;

/**
 * A br.com.nzz.validation builder to build object validators, defining br.com.nzz.validation blocks and chaining validations.<br>
 * A br.com.nzz.validation block represents a group of br.com.nzz.validation rules or error suppliers that will be executed
 * in a row. If a group fails, i.e. has errors, the remaining br.com.nzz.validation blocks will not be executed.<br>
 * The builder will allow any error supplier and any br.com.nzz.validation rules typed with the same target type
 * or any of it's superclass.<br>
 * <pre>
 * Example:
 * If a given object {@code A} extends {@code B} that extends {@link Object}, you can chain:
 * - rules that validate {@link Object};
 * - rules that validate {@code B};
 * - rules that validate {@code A};
 * - any error supplier.
 * </pre>
 *
 * @param <T> the builder's br.com.nzz.validation target
 * @author Luiz.Nazari
 */
public interface ValidationBuilder<T> {

	/**
	 * Adds a br.com.nzz.validation rules for applying bean br.com.nzz.validation to the current br.com.nzz.validation block.
	 *
	 * @return this
	 */
	ValidationBuilder<T> withBeanValidation();

	/**
	 * Adds a br.com.nzz.validation rules to the current br.com.nzz.validation block.
	 *
	 * @param validationRule the validation rule
	 * @return this
	 */
	ValidationBuilder<T> with(ValidationRule<? super T> validationRule);

	/**
	 * Adds a br.com.nzz.validation rules class to the current br.com.nzz.validation block.<br>
	 * The br.com.nzz.validation rules may be provided by a known Dependency Injection container.
	 *
	 * @param validationRuleClass the validation rule class
	 * @return this
	 */
	ValidationBuilder<T> with(Class<? extends ValidationRule<? super T>> validationRuleClass);

	/**
	 * Adds an error supplier to the current br.com.nzz.validation block.
	 *
	 * @param validationErrorSupplier the validation error supplier
	 * @return this
	 */
	ValidationBuilder<T> with(Supplier<? extends ValidationError> validationErrorSupplier);

	/**
	 * Ends the current br.com.nzz.validation block and starts a new one.<br>
	 * A br.com.nzz.validation block represents a group of br.com.nzz.validation rules or error suppliers that will be executed
	 * in a row. If a group fails, i.e. has errors, the remaining br.com.nzz.validation blocks will not be executed.
	 *
	 * @return this
	 */
	ValidationBuilder<T> blocking();

	/**
	 * Builds the validator with defined br.com.nzz.validation blocks.
	 *
	 * @return the built validator
	 */
	ObjectValidator<T> build();

}
