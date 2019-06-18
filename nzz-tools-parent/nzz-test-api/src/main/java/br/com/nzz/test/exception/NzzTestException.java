package br.com.nzz.test.exception;

/**
 * @author Luiz.Nazari
 */
public class NzzTestException extends RuntimeException {

	private static final long serialVersionUID = -6332089164911904833L;

	public NzzTestException(Throwable throwable) {
		super(throwable);
	}

}
