package br.com.senior.validation;

import br.com.senior.validation.message.ErrorMessage;

import java.util.Optional;

/**
 * A br.com.senior.validation error containing the error code (or error key) and the error message.
 *
 * @author Luiz.Nazari
 */
public interface ValidationError extends ErrorMessage {

	/**
	 * Extracts the error message key out of an string surrounded by '{' and '}'.
	 * @param errorMessageKey the error message original key with braces
	 * @return the error message key
	 */
	default String extractMessageKey(String errorMessageKey) {
		errorMessageKey = Optional
			.ofNullable(errorMessageKey)
			.orElse("{br.com.senior.unknown.error}");

		if (errorMessageKey.startsWith("{")) {
			return errorMessageKey.substring(1, errorMessageKey.length() - 1);
		}
		return errorMessageKey;
	}

}
