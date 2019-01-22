package br.com.senior.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

/**
 * Base class for unit tests with Spring.<br>
 * Provides shorthand methods for easing the writing testes, assertions, instantiate
 * common objects and improves test readability.
 *
 * @author Luiz.Nazari
 */
public abstract class UnitTestSpring extends UnitTest {

	/**
	 * A shorthand for creating a {@link Page}.
	 *
	 * @param objects
	 * @param <T>
	 * @return a {@link Page} containing the given objects.
	 */
	protected <T> Page<T> pageOf(T... objects) {
		return new PageImpl<>(Arrays.asList(objects));
	}

}
