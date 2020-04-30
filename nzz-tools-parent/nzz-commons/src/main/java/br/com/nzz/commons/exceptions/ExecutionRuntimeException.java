package br.com.nzz.commons.exceptions;

public class ExecutionRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1112227096690934130L;

	public ExecutionRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
