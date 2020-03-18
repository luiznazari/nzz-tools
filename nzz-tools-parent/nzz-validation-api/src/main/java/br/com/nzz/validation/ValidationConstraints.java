package br.com.nzz.validation;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import br.com.nzz.validation.impl.I18nValidationError;
import br.com.nzz.validation.utils.StringUtils;

/**
 * Provides common br.com.nzz.validation errors suppliers.
 *
 * @author Luiz.Nazari
 */
public abstract class ValidationConstraints {

	private static final String ERROR_START_DATE_AFTER_END_DATE = "error.start.date.after.end.date";

	private ValidationConstraints() {
	}

	public static Supplier<ValidationError> notEmpty(@Nullable String string, String errorMessageKey) {
		return mustBeFalse(() -> StringUtils.isEmpty(string), errorMessageKey);
	}

	public static Supplier<ValidationError> notEmpty(Collection<?> collection, String errorMessageKey) {
		return mustBeFalse(() -> collection == null || collection.isEmpty(), errorMessageKey);
	}

	public static Supplier<ValidationError> mustBeTrue(Boolean condition, String errorMessageKey) {
		return mustBeFalse(() -> !condition, errorMessageKey);
	}

	public static Supplier<ValidationError> mustBeFalse(Boolean condition, String errorMessageKey) {
		return mustBeFalse(() -> condition, errorMessageKey);
	}

	private static Supplier<ValidationError> mustBeFalse(Supplier<Boolean> condition, String errorMessageKey) {
		return () -> condition.get() ? new I18nValidationError(errorMessageKey) : null;
	}

	public static <T extends Temporal & Comparable<T>> Supplier<ValidationError> startDateEqualsOrBeforeEndDate(final T start, final T end) {
		return () -> {
			if (start != null && end != null && start.compareTo(end) > 0) {
				return new I18nValidationError(ERROR_START_DATE_AFTER_END_DATE);
			}
			return null;
		};
	}

}
