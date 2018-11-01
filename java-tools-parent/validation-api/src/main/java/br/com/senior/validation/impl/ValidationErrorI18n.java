package br.com.senior.validation.impl;

import br.com.senior.validation.ValidationError;
import lombok.Getter;
import lombok.ToString;

import java.util.ResourceBundle;

/**
 * @author Luiz.Nazari
 */
@Getter
@ToString
public class ValidationErrorI18n implements ValidationError {

	private static final long serialVersionUID = -8435320088213529725L;

	private static final ResourceBundle VALIDATION_MESSAGES;

	static {
		VALIDATION_MESSAGES = new NullSafeResourceBundle("ValidationMessages");
	}

	private final String message;
	private final String messageKey;

	public ValidationErrorI18n(String errorMessageKey) {
		this.messageKey = extractMessageKey(errorMessageKey);
		this.message = VALIDATION_MESSAGES.getString(this.messageKey);
	}

}
