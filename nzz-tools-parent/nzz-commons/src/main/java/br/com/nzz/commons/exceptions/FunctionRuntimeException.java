package br.com.nzz.commons.exceptions;

public class FunctionRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6712305628251569506L;

	public FunctionRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
