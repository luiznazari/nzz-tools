package br.com.nzz.validation.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Luiz.Nazari
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = "message")
public class I18nErrorMessage implements ErrorMessage {

	private static final long serialVersionUID = -5200911299762401329L;

	private String message;

	private final String messageKey;

	@JsonIgnore
	private transient Object[] messageParameters = new Object[0];

	public I18nErrorMessage(String messageKey) {
		this.messageKey = messageKey;
	}

	public I18nErrorMessage parameters(Object... messageParameters) {
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

}
