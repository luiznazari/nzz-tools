package br.com.nzz.validation.impl;

import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.message.I18nErrorMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Luiz.Nazari
 */
@Getter
@ToString
public class I18nValidationError extends I18nErrorMessage implements ValidationError {

	private static final long serialVersionUID = 8688707214492299507L;

	public I18nValidationError(@NonNull String errorMessageKey, Object... params) {
		super(errorMessageKey);
		this.params(params);
	}

	@Override
	public I18nValidationError params(Object... messageParameters) {
		super.params(messageParameters);
		return this;
	}

}
