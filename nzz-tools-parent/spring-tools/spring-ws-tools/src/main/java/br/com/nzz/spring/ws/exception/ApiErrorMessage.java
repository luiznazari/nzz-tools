package br.com.nzz.spring.ws.exception;

import br.com.nzz.validation.message.ErrorMessage;
import br.com.nzz.validation.message.I18n;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiErrorMessage implements ErrorMessage {

	private static final long serialVersionUID = 3808686588461796047L;

	private String message;
	private final String messageKey;
	private transient Object[] messageParameters = new Object[0];

	public ApiErrorMessage(String messageKey) {
		this.messageKey = messageKey;
	}

	public ApiErrorMessage(String messageKey, String message) {
		this(messageKey);
		this.message = message;
	}

	public ApiErrorMessage params(Object... messageParameters) {
		this.messageParameters = messageParameters;
		this.message = null;
		return this;
	}

	public String getMessage() {
		if (this.message == null) {
			this.message = I18n.getMessage(this.messageKey, this.messageParameters);
		}

		return this.message;
	}

	public List<ErrorMessage> asErrorList() {
		return Lists.newArrayList(this);
	}

	public static ApiErrorMessage from(ErrorMessage errorMessage) {
		return new ApiErrorMessage(errorMessage.getMessageKey(), errorMessage.getMessage());
	}

}
