package br.com.nzz.spring.exception;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import br.com.nzz.validation.message.ErrorMessage;
import lombok.Getter;

/**
 * Exceção lançada pelos serviços que manipulam WebServices, suas requisições e respostas.
 *
 * @author Luiz Felipe Nazari
 */
public class WebServiceException extends RuntimeException {

	private static final long serialVersionUID = -5601844939372278210L;

	@Getter
	private final List<Serializable> parameters = Lists.newLinkedList();

	public WebServiceException(String errorMessageKey) {
		this(errorMessageKey, null);
	}

	public WebServiceException(String errorMessageKey, Throwable throwable) {
		super(errorMessageKey, throwable);
	}

	public WebServiceException parameters(Serializable... params) {
		this.parameters.addAll(Arrays.asList(params));
		return this;
	}

	public ApiErrorMessage toErrorMessage() {
		return new ApiErrorMessage(this.getMessage()).params(this.parameters.toArray());
	}

}
