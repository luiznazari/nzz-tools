package br.com.nzz.spring.ws.utils;

/**
 * An unsafe version of {@link java.util.function.Consumer}, can throw any
 * {@link Exception} when applied.
 *
 * @param <T> the type of the input to the operation
 * @author Luiz.Nazari
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface UnSafeConsumer<T> {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	void accept(T t) throws Exception;

}