package br.com.nzz.spring.exception;

import lombok.Getter;

/**
 * Exceção lançada pelos serviços que manipulam WebServices, suas requisições e respostas.
 *
 * @author Luiz Felipe Nazari
 */
public class WebServiceException extends RuntimeException {

	private static final long serialVersionUID = -5601844939372278210L;

	@Getter
	private ApiErrorMessage error;

	public WebServiceException(String errorMessageKey) {
		this(errorMessageKey, null);
	}

	public WebServiceException(String errorMessageKey, Throwable throwable) {
		super(errorMessageKey, throwable);
	}

	public WebServiceException parameters(Object... params) {
		this.error = new ApiErrorMessage(this.getMessage()).params(params);
		return this;
	}

}
