package br.com.nzz.commons.function;

import org.junit.Test;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;

import static org.junit.Assert.assertEquals;

public class UnsafeFunctionTest {

	private final UnsafeFunction<String, Double, NzzTestException> function =
		number -> {
			try {
				return Double.parseDouble(number);
			} catch (NumberFormatException e) {
				throw new NzzTestException(e.getMessage());
			}
		};

	@Test
	public void apply() throws NzzTestException {
		assertEquals(.42, function.apply(".42"), 0.0);
	}

	@Test(expected = NzzTestException.class)
	public void applyThrowingException() throws NzzTestException {
		function.apply("not a number");
	}

	@Test(expected = FunctionRuntimeException.class)
	public void toFunction() {
		function.toFunction().apply("not a number");
	}

}