package br.com.nzz.spring.exception;

/**
 * @author Luiz.Nazari
 */
public class WebServiceEntityNotFoundException extends WebServiceException {

	private static final long serialVersionUID = 7769179831860137912L;

	public WebServiceEntityNotFoundException(String errorMessageKey) {
		super(errorMessageKey);
	}

}
