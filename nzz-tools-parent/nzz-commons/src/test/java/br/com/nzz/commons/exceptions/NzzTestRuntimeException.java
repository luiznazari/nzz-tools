package br.com.nzz.commons.exceptions;

/**
 * A all-purpose runtime exception for tests.
 *
 * @author Luiz.Nazari
 */
public class NzzTestRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8631676810249395439L;

	public NzzTestRuntimeException(String message) {
		super(message);
	}

}
