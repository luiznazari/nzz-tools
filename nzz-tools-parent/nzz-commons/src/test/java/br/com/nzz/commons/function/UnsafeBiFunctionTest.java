package br.com.nzz.commons.function;

import org.junit.Test;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;

import static org.junit.Assert.assertEquals;

public class UnsafeBiFunctionTest {

	private final UnsafeBiFunction<String, Double, Double, NzzTestException> function =
		(number, defaultValue) -> {
			try {
				if (number == null) {
					return defaultValue;
				}
				return Double.parseDouble(number);
			} catch (NumberFormatException e) {
				throw new NzzTestException(e.getMessage());
			}
		};

	@Test
	public void apply() throws NzzTestException {
		assertEquals(.42, function.apply(".42", .42), 0.0);
	}

	@Test
	public void applyNull() throws NzzTestException {
		assertEquals(.42, function.apply(null, .42), 0.0);
	}

	@Test(expected = NzzTestException.class)
	public void applyThrowingException() throws NzzTestException {
		function.apply("not a number", .42);
	}

	@Test(expected = FunctionRuntimeException.class)
	public void toBiFunction() {
		function.toBiFunction().apply("not a number", .42);
	}

}