package br.com.nzz.commons.function;

import java.util.function.Consumer;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;

/**
 * An alternative to the java functional {@link java.util.function.Consumer} that may throw a
 * checked exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <P> the type of the input to the operation
 * @param <T> the type the error that may be thrown by this consumer
 * @author Luiz.Nazari
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface UnsafeConsumer<P, T extends Throwable> {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param p the input argument
	 * @throws T the error type that may be thrown by this function
	 */
	void accept(P p) throws T;

	/**
	 * Converts this {@link UnsafeConsumer} to a {@link Consumer}, capturing checked exceptions and
	 * throwing a unchecked exception of type {@link FunctionRuntimeException} instead.
	 *
	 * @return a consumer
	 */
	@SuppressWarnings("squid:S1181")
	default Consumer<P> toConsumer() {
		return p -> {
			try {
				accept(p);
			} catch (Throwable t) {
				throw new FunctionRuntimeException(t);
			}
		};
	}

}

