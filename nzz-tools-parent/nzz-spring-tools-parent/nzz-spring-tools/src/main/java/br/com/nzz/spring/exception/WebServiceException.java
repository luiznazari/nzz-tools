package br.com.nzz.spring.exception;

import lombok.Getter;

/**
 * Exceção lançada pelos serviços que manipulam WebServices, suas requisições e respostas.
 *
 * @author Luiz Felipe Nazari
 */
public class WebServiceException extends RuntimeException {

	private static final long serialVersionUID = -806298005650580948L;

	@Getter
	private final ApiErrorMessage error;

	public WebServiceException(String errorMessageKey, Object... params) {
		this(null, errorMessageKey, params);
	}

	public WebServiceException(Throwable throwable, String errorMessageKey, Object... params) {
		super(errorMessageKey, throwable);
		this.error = new ApiErrorMessage(errorMessageKey).params(params);
	}

}
