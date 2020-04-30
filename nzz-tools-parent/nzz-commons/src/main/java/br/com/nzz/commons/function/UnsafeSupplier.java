package br.com.nzz.commons.function;

import java.util.function.Supplier;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;

/**
 * An alternative to the java functional {@link java.util.function.Supplier} that may throw a
 * checked exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <R> the type of the result of the function
 * @param <T> the type the error that may be thrown by this supplier
 * @author Luiz.Nazari
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface UnsafeSupplier<R, T extends Throwable> {

	/**
	 * Gets a result.
	 *
	 * @return a result
	 * @throws T the error type that may be thrown by this function
	 */
	R get() throws T;

	/**
	 * Converts this {@link UnsafeSupplier} to a {@link Supplier}, capturing checked exceptions and
	 * throwing a unchecked exception of type {@link FunctionRuntimeException} instead.
	 *
	 * @return a supplier
	 */
	@SuppressWarnings("squid:S1181")
	default Supplier<R> toSupplier() {
		return () -> {
			try {
				return get();
			} catch (Throwable t) {
				throw new FunctionRuntimeException(t);
			}
		};
	}

}

