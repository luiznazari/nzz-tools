package br.com.senior.validation;

import java.util.function.Supplier;

/**
 * A br.com.senior.validation builder to build object validators, defining br.com.senior.validation blocks and chaining validations.<br>
 * A br.com.senior.validation block represents a group of br.com.senior.validation rules or error suppliers that will be executed
 * in a row. If a group fails, i.e. has errors, the remaining br.com.senior.validation blocks will not be executed.<br>
 * The builder will allow any error supplier and any br.com.senior.validation rules typed with the same target type
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
 * @param <T> the builder's br.com.senior.validation target
 * @author Luiz.Nazari
 */
public interface ValidationBuilder<T> {

	/**
	 * Adds a br.com.senior.validation rules for applying bean br.com.senior.validation to the current br.com.senior.validation block.
	 *
	 * @return this
	 */
	ValidationBuilder<T> withBeanValidation();

	/**
	 * Adds a br.com.senior.validation rules to the current br.com.senior.validation block.
	 *
	 * @return this
	 */
	ValidationBuilder<T> with(ValidationRule<? super T> validationRule);

	/**
	 * Adds a br.com.senior.validation rules class to the current br.com.senior.validation block.<br>
	 * The br.com.senior.validation rules may be provided by a known Dependency Injection container.
	 *
	 * @return this
	 */
	ValidationBuilder<T> with(Class<? extends ValidationRule<? super T>> validationRuleClass);

	/**
	 * Adds an error supplier to the current br.com.senior.validation block.
	 *
	 * @return this
	 */
	ValidationBuilder<T> with(Supplier<? extends ValidationError> validationErrorSupplier);

	/**
	 * Ends the current br.com.senior.validation block and starts a new one.<br>
	 * A br.com.senior.validation block represents a group of br.com.senior.validation rules or error suppliers that will be executed
	 * in a row. If a group fails, i.e. has errors, the remaining br.com.senior.validation blocks will not be executed.
	 *
	 * @return this
	 */
	ValidationBuilder<T> blocking();

	/**
	 * Builds the validator with defined br.com.senior.validation blocks.
	 *
	 * @return the built validator
	 */
	ObjectValidator<T> build();

}
