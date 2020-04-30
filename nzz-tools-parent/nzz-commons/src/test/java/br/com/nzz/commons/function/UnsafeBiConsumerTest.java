package br.com.nzz.commons.function;

import org.junit.Test;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;

public class UnsafeBiConsumerTest {

	private final UnsafeBiConsumer<String, String, NzzTestException> consumer =
		(number, errorMessage) -> {
			try {
				Double.parseDouble(number);
			} catch (NumberFormatException e) {
				throw new NzzTestException(errorMessage, e);
			}
		};

	@Test
	@SuppressWarnings("squid:S2699")
	public void accept() throws NzzTestException {
		consumer.accept("1", "Parsing error.");
	}

	@Test(expected = NzzTestException.class)
	public void acceptThrowingException() throws NzzTestException {
		consumer.accept("not a number", "Parsing error.");
	}

	@Test(expected = FunctionRuntimeException.class)
	public void toBiConsumer() {
		consumer.toBiConsumer().accept("not a number", "Parsing error");
	}

}