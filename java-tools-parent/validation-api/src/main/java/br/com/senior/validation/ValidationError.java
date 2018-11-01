package br.com.senior.validation;

import java.io.Serializable;
import java.util.Optional;

/**
 * A br.com.senior.validation error containing the error code (or error key) and the error message.
 *
 * @author Luiz.Nazari
 */
public interface ValidationError extends Serializable {

	/**
	 * @return the error message key
	 */
	String getMessageKey();

	/**
	 * @return the error, user friendly, message
	 */
	String getMessage();

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
