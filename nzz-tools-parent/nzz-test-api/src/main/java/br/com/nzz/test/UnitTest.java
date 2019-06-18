package br.com.nzz.test;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

/**
 * Base class for unit tests.<br>
 * Provides shorthand methods for easing the writing testes, assertions, instantiate
 * common objects and improves test readability.
 *
 * @author Luiz.Nazari
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTest {

	private static final AtomicLong ID_SEQUENCE = new AtomicLong(1);

	public static Long nextId() {
		return ID_SEQUENCE.getAndIncrement();
	}

	/**
	 * A shorthand for {@link Mockito#mock(Class)}.
	 *
	 * @param classToMock class or interface to mock
	 * @param <T>         type of the mock
	 * @return mock object
	 */
	public <T> T mock(Class<T> classToMock) {
		return Mockito.mock(classToMock);
	}


	/**
	 * A shorthand for {@link Mockito#spy(Class)}.
	 *
	 * @param classToSpy the class to spy
	 * @param <T>        type of the spy
	 * @return a spy of the provided class
	 */
	public <T> T spy(Class<T> classToSpy) {
		return Mockito.spy(classToSpy);
	}

	/**
	 * A shorthand for {@link Mockito#spy(Object)}.
	 *
	 * @param object the object to spy
	 * @param <T>    type of the spy
	 * @return a spy of the provided class
	 */
	public <T> T spy(T object) {
		return Mockito.spy(object);
	}


	/**
	 * A shorthand for {@link Mockito#when(Object)}.
	 *
	 * @param methodCall method to be stubbed
	 * @param <T>        type of the method's return value
	 * @return OngoingStubbing object used to stub fluently.
	 * <strong>Do not</strong> create a reference to this returned object.
	 */
	public <T> OngoingStubbing<T> when(T methodCall) {
		return Mockito.when(methodCall);
	}

	/**
	 * A shorthand for {@link Mockito#verify(Object)}.
	 *
	 * @param mock to be verified
	 * @param <T>  type of the mock
	 * @return mock object itself
	 */
	public <T> T verify(T mock) {
		return Mockito.verify(mock);
	}

	/**
	 * A shorthand for {@link Mockito#verify(Object, VerificationMode)}.
	 *
	 * @param mock             to be verified
	 * @param verificationMode times(x), atLeastOnce() or never()
	 * @param <T>              type of the mock
	 * @return mock object itself
	 */
	public <T> T verify(T mock, VerificationMode verificationMode) {
		return Mockito.verify(mock, verificationMode);
	}

	/**
	 * A shorthand for {@link Mockito#never()}.
	 *
	 * @return verification mode never()
	 */
	public VerificationMode never() {
		return Mockito.never();
	}

	/**
	 * A shorthand for {@link Mockito#times(int) Mockito.times(1)}.
	 *
	 * @return verification mode times(1)
	 */
	public VerificationMode once() {
		return Mockito.times(1);
	}

	/**
	 * A shorthand for {@link Mockito#times(int)}.
	 *
	 * @param wantedNumberOfInvocations wanted number of invocations
	 * @return verification mode
	 */
	public VerificationMode times(int wantedNumberOfInvocations) {
		return Mockito.times(wantedNumberOfInvocations);
	}


	/**
	 * A shorthand for {@link Mockito#any(Class)}.
	 *
	 * @param clazz The type to avoid casting
	 * @param <T>   type of the matcher
	 * @return <code>null</code>.
	 */
	protected <T> T any(Class<T> clazz) {
		return Mockito.any(clazz);
	}

	/**
	 * A shorthand for {@link Mockito#eq(Object)}.
	 *
	 * @param object the given value.
	 * @param <T>    type of the matcher
	 * @return <code>null</code>.
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

	public static void fail(Throwable throwable) {
		log.error(throwable.getMessage(), throwable);
		fail(throwable.getMessage());
	}

	public static void fail(String failMessage) {
		Assert.fail(failMessage);
	}

}
