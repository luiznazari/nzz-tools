package br.com.nzz.commons.function;

import org.junit.Test;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;

import static org.junit.Assert.assertEquals;

public class UnsafeSupplierTest {

	@Test
	public void get() throws NzzTestException {
		final UnsafeSupplier<Double, NzzTestException> supplier =
			() -> .42;

		assertEquals(.42, supplier.get(), 0.0);
	}

	@Test(expected = NzzTestException.class)
	public void getThrowingException() throws NzzTestException {
		final UnsafeSupplier<Double, NzzTestException> supplier =
			() -> {
				throw new NzzTestException("No .42 here!");
			};

		supplier.get();
	}

	@Test(expected = FunctionRuntimeException.class)
	public void toSupplier() {
		final UnsafeSupplier<Double, NzzTestException> supplier =
			() -> {
				throw new NzzTestException("No .42 here!");
			};

		supplier.toSupplier().get();
	}

}