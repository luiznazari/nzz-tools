package br.com.nzz.commons.function;

import java.util.function.Function;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;

/**
 * An alternative to the java functional {@link java.util.function.Function} that may throw a
 * checked exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <P> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <T> the type the error that may be thrown by this function
 * @author Luiz.Nazari
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface UnsafeFunction<P, R, T extends Throwable> {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param p the type of the input to the function
	 * @return the function result
	 * @throws T the error type that may be thrown by this function
	 */
	R apply(P p) throws T;

	/**
	 * Converts this {@link UnsafeFunction} to a {@link Function}capturing checked exceptions and
	 * throwing a unchecked exception of type {@link FunctionRuntimeException} instead.
	 *
	 * @return a function
	 */
	@SuppressWarnings("squid:S1181")
	default Function<P, R> toFunction() {
		return p -> {
			try {
				return apply(p);
			} catch (Throwable t) {
				throw new FunctionRuntimeException(t);
			}
		};
	}

}

