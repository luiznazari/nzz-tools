package br.com.nzz.commons.function;

import java.util.function.BiFunction;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;

/**
 * An alternative to the java functional {@link java.util.function.BiFunction} that may throw a
 * checked exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <P> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <T> the type the error that may be thrown by this function
 * @author Luiz.Nazari
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface UnsafeBiFunction<P, U, R, T extends Throwable> {

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param p the first function argument
	 * @param u the second function argument
	 * @return the function result
	 * @throws T the error type that may be thrown by this function
	 */
	R apply(P p, U u) throws T;

	/**
	 * Converts this {@link UnsafeBiFunction} to a {@link BiFunction}, capturing checked exceptions
	 * and throwing a unchecked exception of type {@link FunctionRuntimeException} instead.
	 *
	 * @return a bi-function
	 */
	@SuppressWarnings("squid:S1181")
	default BiFunction<P, U, R> toBiFunction() {
		return (p, u) -> {
			try {
				return apply(p, u);
			} catch (Throwable t) {
				throw new FunctionRuntimeException(t);
			}
		};
	}

}

