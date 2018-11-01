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
public interface ValidationObjectBuilder<T> extends ValidationBuilder<T> {

	/**
	 * A shorthand version of:
	 * <pre>
	 * validationBuilder.build().validate(object);
	 * </pre>
	 * Where the "object" is the builder's target object.
	 *
	 * @return the br.com.senior.validation result
	 */
	ValidationResult validate();

	@Override
	ValidationObjectBuilder<T> blocking();

	@Override
	ValidationObjectBuilder<T> withBeanValidation();

	@Override
	ValidationObjectBuilder<T> with(ValidationRule<? super T> validationRule);

	@Override
	ValidationObjectBuilder<T> with(Class<? extends ValidationRule<? super T>> validationRuleClass);

	@Override
	ValidationObjectBuilder<T> with(Supplier<? extends ValidationError> validationErrorSupplier);

}
