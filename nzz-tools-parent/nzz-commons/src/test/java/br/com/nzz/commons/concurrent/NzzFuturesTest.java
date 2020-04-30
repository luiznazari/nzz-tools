package br.com.nzz.commons.concurrent;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import br.com.nzz.commons.exceptions.ExecutionRuntimeException;
import br.com.nzz.commons.exceptions.NzzTestException;
import br.com.nzz.commons.exceptions.NzzTestRuntimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NzzFuturesTest {

	private CompletableFuture<Double> doubleCompletableFuture;
	private CompletableFuture<String> stringCompletableFuture;
	private CompletableFuture<Void> voidCompletableFuture;

	@Before
	public void init() {
		this.doubleCompletableFuture = CompletableFuture.supplyAsync(() -> .42);
		this.stringCompletableFuture = CompletableFuture.completedFuture("42");
		this.voidCompletableFuture = CompletableFuture.runAsync(() -> {
			throw new NzzTestRuntimeException("Ops!");
		});
	}

	@Test
	public void allOf() {
		NzzFutures.allOf(Arrays.asList(
			doubleCompletableFuture,
			stringCompletableFuture,
			voidCompletableFuture
		)).thenAccept(v -> {
			assertTrue(doubleCompletableFuture.isDone());
			assertTrue(stringCompletableFuture.isDone());
			assertTrue(voidCompletableFuture.isDone());
		});
	}

	@Test
	public void allList() {
		NzzFutures.all(Arrays.asList(
			doubleCompletableFuture,
			stringCompletableFuture,
			voidCompletableFuture
		)).thenAccept(this::assertAllResults);
	}

	@Test
	public void allArray() {
		CompletableFuture<?>[] futures = {
			doubleCompletableFuture,
			stringCompletableFuture,
			voidCompletableFuture
		};
		NzzFutures.all(futures).thenAccept(this::assertAllResults);
	}

	@Test
	public void allVarargs() {
		NzzFutures.all(
			doubleCompletableFuture,
			stringCompletableFuture,
			voidCompletableFuture
		).thenAccept(this::assertAllResults);
	}

	private void assertAllResults(List<FutureResult<?>> results) {
		assertTrue(doubleCompletableFuture.isDone());
		assertTrue(stringCompletableFuture.isDone());
		assertTrue(voidCompletableFuture.isDone());

		assertEquals(3, results.size());
		assertEquals(.42, (Double) results.get(0).getResult(), 0.0);
		assertNull(results.get(0).getThrowable());

		assertEquals("42", results.get(1).getResult());
		assertNull(results.get(1).getThrowable());

		assertNull(results.get(2).getResult());
		assertEquals("Ops!", results.get(2).getThrowable().getMessage());
	}

	@Test
	public void isFailed() {
		CompletableFuture<Void> uncompleted = new CompletableFuture<>();
		CompletableFuture<Void> canceled = new CompletableFuture<>();
		canceled.cancel(true);

		NzzFutures.awaitQuietly(voidCompletableFuture);

		assertTrue(NzzFutures.isFailed(voidCompletableFuture));
		assertTrue(NzzFutures.isFailed(canceled));
		assertFalse(NzzFutures.isFailed(doubleCompletableFuture));
		assertFalse(NzzFutures.isFailed(uncompleted));
	}

	@Test
	public void completedExceptionally() {
		CompletableFuture<Void> completed = NzzFutures.completedExceptionally(
			new IOException("test error"));
		assertTrue(completed.isCompletedExceptionally());
		assertTrue(NzzFutures.isFailed(completed));
	}

	@Test
	public void shouldAwaitQuietlyCompletableFuture() {
		CompletableFuture<Void> completedExceptionally = NzzFutures
			.completedExceptionally(new NzzTestException("test error"));

		NzzFutures.awaitQuietly(completedExceptionally);
		assertTrue(true);
	}

	@Test
	public void shouldAwaitQuietlyCompletableFutureCollection() {
		NzzFutures.awaitQuietly(Arrays.asList(doubleCompletableFuture, stringCompletableFuture));
		assertTrue(true);
	}

	@Test
	public void shouldAwaitCompletableFuture() {
		NzzTestException expectedException = new NzzTestException("test error");
		CompletableFuture<Void> completedExceptionally = NzzFutures
			.completedExceptionally(expectedException);

		try {
			NzzFutures.await(completedExceptionally);
			fail("Should have thrown an exception!");
		} catch (ExecutionRuntimeException e) {
			assertEquals(expectedException, e.getCause());
		}

		try {
			NzzFutures.await(stringCompletableFuture);
		} catch (ExecutionRuntimeException e) {
			fail("Should not have thrown an exception!");
		}
	}

	@Test
	public void shouldAwaitCompletableFutureCollection() {
		NzzTestException expectedException = new NzzTestException("test error");
		CompletableFuture<Void> completedExceptionally = NzzFutures
			.completedExceptionally(expectedException);

		try {
			NzzFutures.await(Arrays.asList(
				stringCompletableFuture,
				completedExceptionally));
			fail("Should have thrown an exception!");
		} catch (ExecutionRuntimeException e) {
			assertEquals(expectedException, e.getCause());
		}

		try {
			NzzFutures.await(Arrays.asList(
				stringCompletableFuture,
				doubleCompletableFuture));
		} catch (ExecutionRuntimeException e) {
			e.printStackTrace();
			fail("Should not have thrown an exception!");
		}
	}

}