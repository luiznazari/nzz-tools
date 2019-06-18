package br.com.nzz.validation.exception;

import br.com.nzz.validation.message.ErrorMessage;

import java.util.Set;

/**
 * An ErrorMessageException represents an exception thrown because some error had occurred at
 * runtime in the application. This may be a validation error or a custom {@link ErrorMessage error}.
 *
 * @author Luiz.Nazari
 */
public abstract class ErrorMessageException extends RuntimeException {

	private static final long serialVersionUID = 1898384696418403743L;

	protected ErrorMessageException() {
		super();
	}

	protected ErrorMessageException(String message) {
		super(message);
	}

	protected ErrorMessageException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public abstract <E extends ErrorMessage> Set<E> getErrors();

}
