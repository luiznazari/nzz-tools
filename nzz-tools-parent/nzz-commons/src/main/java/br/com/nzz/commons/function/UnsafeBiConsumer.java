package br.com.nzz.commons.function;

import java.util.function.BiConsumer;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;

/**
 * An alternative to the java functional {@link java.util.function.BiConsumer} that may throw a
 * checked exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <P> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <T> the type the error that may be thrown by this consumer
 * @author Luiz.Nazari
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface UnsafeBiConsumer<P, U, T extends Throwable> {

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param p the first input argument
	 * @param u the second input argument
	 * @throws T the error type that may be thrown by this function
	 */
	void accept(P p, U u) throws T;

	/**
	 * Converts this {@link UnsafeBiConsumer} to a {@link BiConsumer}, capturing checked exceptions
	 * and throwing a unchecked exception of type {@link FunctionRuntimeException} instead.
	 *
	 * @return a bi-consumer
	 */
	@SuppressWarnings("squid:S1181")
	default BiConsumer<P, U> toBiConsumer() {
		return (p, u) -> {
			try {
				accept(p, u);
			} catch (Throwable t) {
				throw new FunctionRuntimeException(t);
			}
		};
	}

}

