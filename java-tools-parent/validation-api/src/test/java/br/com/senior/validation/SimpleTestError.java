package br.com.senior.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleTestError implements ValidationError {

	public static final String DEFAULT_ERROR_MSG_CODE = "test.random.error.msg.code";

	private static final long serialVersionUID = -8430397613465939812L;

	private final String errorMessageCode;

	public SimpleTestError() {
		this.errorMessageCode = DEFAULT_ERROR_MSG_CODE;
	}

	@Override
	public String getMessageKey() {
		return this.errorMessageCode;
	}

	@Override
	public String getMessage() {
		return this.errorMessageCode;
	}

}
