package br.com.senior.test;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Base class for unit tests.<br>
 * Provides shorthand methods for easing the writing testes, assertions, instantiate
 * common objects and improves test readability.
 *
 * @author Luiz.Nazari
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTest {

	private static final AtomicLong ID_SEQUENCE = new AtomicLong(1);

	public static Long nextId() {
		return ID_SEQUENCE.getAndIncrement();
	}

	/**
	 * A shorthand for {@link Mockito#mock(Class)}.
	 *
	 * @param classToMock
	 * @param <T>
	 * @return
	 */
	public <T> T mock(Class<T> classToMock) {
		return Mockito.mock(classToMock);
	}


	/**
	 * A shorthand for {@link Mockito#spy(Class)}.
	 *
	 * @param classToSpy
	 * @param <T>
	 * @return
	 */
	public <T> T spy(Class<T> classToSpy) {
		return Mockito.spy(classToSpy);
	}

	/**
	 * A shorthand for {@link Mockito#spy(Object)}.
	 *
	 * @param object
	 * @param <T>
	 * @return
	 */
	public <T> T spy(T object) {
		return Mockito.spy(object);
	}


	/**
	 * A shorthand for {@link Mockito#when(Object)}.
	 *
	 * @param methodCall
	 * @param <T>
	 * @return
	 */
	public <T> OngoingStubbing<T> when(T methodCall) {
		return Mockito.when(methodCall);
	}

	/**
	 * A shorthand for {@link Mockito#verify(Object)}.
	 *
	 * @param mock
	 * @param <T>
	 * @return
	 */
	public <T> T verify(T mock) {
		return Mockito.verify(mock);
	}

	/**
	 * A shorthand for {@link Mockito#verify(Object, VerificationMode)}.
	 *
	 * @param mock
	 * @param verificationMode
	 * @param <T>
	 * @return
	 */
	public <T> T verify(T mock, VerificationMode verificationMode) {
		return Mockito.verify(mock, verificationMode);
	}

	/**
	 * A shorthand for {@link Mockito#never()}.
	 *
	 * @return
	 */
	public VerificationMode never() {
		return Mockito.never();
	}

	/**
	 * A shorthand for {@link Mockito#times(int) Mockito.times(1)}.
	 *
	 * @return
	 */
	public VerificationMode once() {
		return Mockito.times(1);
	}

	/**
	 * A shorthand for {@link Mockito#times(int)}.
	 *
	 * @param wantedNumberOfInvocations
	 * @return
	 */
	public VerificationMode times(int wantedNumberOfInvocations) {
		return Mockito.times(wantedNumberOfInvocations);
	}


	/**
	 * A shorthand for {@link Mockito#any()}.
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	protected <T> T any(Class<T> clazz) {
		return Mockito.any(clazz);
	}

	/**
	 * A shorthand for {@link Mockito#eq(Object)}.
	 *
	 * @param object
	 * @param <T>
	 * @return
	 */
	protected <T> T eq(T object) {
		return Mockito.eq(object);
	}

	@SuppressWarnings("rawtypes")
	protected Answer withFirstParameter() {
		return invocation -> {
			if (invocation.getArguments().length > 0) {
				return invocation.getArguments()[0];
			}
			return null;
		};
	}

}
