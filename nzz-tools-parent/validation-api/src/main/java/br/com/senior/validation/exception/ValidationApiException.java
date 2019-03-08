package br.com.senior.validation.exception;

/**
 * Generic exception thrown by validation API.
 *
 * @author Luiz.Nazari
 */
public class ValidationApiException extends RuntimeException {

	private static final long serialVersionUID = 6632588928369713618L;

	public ValidationApiException(String message) {
		super(message);
	}

}
