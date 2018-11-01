package br.com.senior.validation;

import br.com.senior.validation.impl.ValidationErrorI18n;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Provides common br.com.senior.validation errors suppliers.
 *
 * @author Luiz.Nazari
 */
public abstract class ValidationConstraints {

	private ValidationConstraints() {
	}

	public static Supplier<ValidationError> notEmpty(String string, String errorMessageKey) {
		return mustBeFalse(() -> StringUtils.isEmpty(string), errorMessageKey);
	}

	public static Supplier<ValidationError> notEmpty(Collection<?> collection, String errorMessageKey) {
		return mustBeFalse(() -> CollectionUtils.isEmpty(collection), errorMessageKey);
	}

	public static Supplier<ValidationError> mustBeTrue(Boolean condition, String errorMessageKey) {
		return mustBeFalse(() -> !condition, errorMessageKey);
	}

	public static Supplier<ValidationError> mustBeFalse(Boolean condition, String errorMessageKey) {
		return mustBeFalse(() -> condition, errorMessageKey);
	}

	private static Supplier<ValidationError> mustBeFalse(Supplier<Boolean> condition, String errorMessageKey) {
		return () -> condition.get() ? new ValidationErrorI18n(errorMessageKey) : null;
	}

}
