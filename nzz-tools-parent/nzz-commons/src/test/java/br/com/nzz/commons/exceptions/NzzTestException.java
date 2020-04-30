package br.com.nzz.commons.exceptions;

/**
 * A all-purpose exception for tests.
 *
 * @author Luiz.Nazari
 */
public class NzzTestException extends Exception {

	private static final long serialVersionUID = 5489783472959092781L;

	public NzzTestException(String message) {
		super(message);
	}

	public NzzTestException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
