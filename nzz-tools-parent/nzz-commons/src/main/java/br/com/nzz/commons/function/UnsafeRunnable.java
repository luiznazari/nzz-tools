package br.com.nzz.commons.function;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;

/**
 * An alternative to the java functional {@link Runnable} that may throw a checked exception.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #run()}.
 *
 * @param <T> the type the error that may be thrown by this runnable
 * @author Luiz.Nazari
 * @see Runnable
 */
@FunctionalInterface
public interface UnsafeRunnable<T extends Throwable> {

	/**
	 * When an object implementing interface <code>Runnable</code> is used to create a thread,
	 * starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may take any action
	 * whatsoever.
	 *
	 * @throws T the error type that may be thrown by this runnable
	 * @see java.lang.Thread#run()
	 */
	void run() throws T;

	/**
	 * Converts this {@link UnsafeRunnable} to a {@link Runnable},capturing checked exceptions and
	 * throwing a unchecked exception of type {@link FunctionRuntimeException} instead.
	 *
	 * @return a runnable
	 */
	@SuppressWarnings("squid:S1181")
	default Runnable toRunnable() {
		return () -> {
			try {
				run();
			} catch (Throwable t) {
				throw new FunctionRuntimeException(t);
			}
		};
	}

}