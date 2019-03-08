package br.com.nzz.validation.exception;

/**
 * Generic exception thrown by validation API.
 *
 * @author Luiz.Nazari
 */
public class CustomValidationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6632588928369713618L;

	public CustomValidationRuntimeException(String message) {
		super(message);
	}

	public CustomValidationRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
