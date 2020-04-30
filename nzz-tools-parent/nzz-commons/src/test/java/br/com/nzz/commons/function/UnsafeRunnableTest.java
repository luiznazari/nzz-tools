package br.com.nzz.commons.function;

import org.junit.Test;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;

public class UnsafeRunnableTest {

	@Test
	@SuppressWarnings("squid:S2699")
	public void run() throws NzzTestException {
		UnsafeRunnable<NzzTestException> runnable = () -> {
			// Some well written process.
		};

		runnable.run();
	}

	@Test(expected = NzzTestException.class)
	public void runThrowingException() throws NzzTestException {
		UnsafeRunnable<NzzTestException> runnable = () -> {
			// Some not well written process.
			throw new NzzTestException("Ops!");
		};

		runnable.run();
	}

	@Test(expected = FunctionRuntimeException.class)
	public void toRunnable() {
		UnsafeRunnable<NzzTestException> runnable = () -> {
			// Some not well written process.
			throw new NzzTestException("Ops!");
		};
		runnable.toRunnable().run();
	}

}