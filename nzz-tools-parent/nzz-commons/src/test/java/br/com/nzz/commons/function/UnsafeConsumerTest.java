package br.com.nzz.commons.function;

import org.junit.Test;

import br.com.nzz.commons.exceptions.FunctionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;

import static org.junit.Assert.fail;

public class UnsafeConsumerTest {

	private final UnsafeConsumer<String, NzzTestException> consumer =
		number -> {
			try {
				Double.parseDouble(number);
			} catch (NumberFormatException e) {
				throw new NzzTestException(e.getMessage());
			}
		};

	@Test
	@SuppressWarnings("squid:S2699")
	public void accept() throws NzzTestException {
		consumer.accept("1");
	}

	@Test(expected = NzzTestException.class)
	public void acceptThrowingException() throws NzzTestException {
		consumer.accept("not a number");
	}

	@Test(expected = FunctionRuntimeException.class)
	public void toConsumer() {
		consumer.toConsumer().accept("not a number");
	}

}