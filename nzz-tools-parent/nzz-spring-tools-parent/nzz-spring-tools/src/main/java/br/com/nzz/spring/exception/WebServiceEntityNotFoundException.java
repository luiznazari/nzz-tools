package br.com.nzz.spring.exception;

/**
 * @author Luiz.Nazari
 */
public class WebServiceEntityNotFoundException extends WebServiceException {

	private static final long serialVersionUID = -4464101587046840879L;

	public WebServiceEntityNotFoundException(String errorMessageKey, Object... params) {
		super(errorMessageKey, params);
	}

}
