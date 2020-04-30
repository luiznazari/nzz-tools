package br.com.nzz.commons.concurrent;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import br.com.nzz.commons.exceptions.ExecutionRuntimeException;

/**
 * Utility class to manipulate Java's {@link java.util.concurrent.Future} and
 * {@link CompletableFuture}. It offers utility methods to easy the usage of
 * Java's Future API as well as making code cleaner.
 *
 * @author Luiz Felipe Nazari
 */
@SuppressWarnings("WeakerAccess") // public lib
public abstract class NzzFutures {

	private static final Log log = LogFactory.getLog(NzzFutures.class);

	private NzzFutures() {
	}

	@SuppressWarnings("squid:S1452") // usage of generic wildcard type
	static <T> CompletableFuture<List<FutureResult<? extends T>>> all(Collection<CompletableFuture<? extends T>> futuresList) {
		return CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]))
			.thenApplyAsync(v ->
				futuresList.stream()
					.map(NzzFutures::getSafeResult)
					.collect(Collectors.toList())
			);
	}

	@SafeVarargs
	@SuppressWarnings("squid:S1452") // usage of generic wildcard type
	static <T> CompletableFuture<List<FutureResult<? extends T>>> all(CompletableFuture<? extends T>... futures) {
		return CompletableFuture.allOf(futures)
			.thenApplyAsync(v ->
				Stream.of(futures)
					.map(NzzFutures::getSafeResult)
					.collect(Collectors.toList())
			);
	}

	static <T> CompletableFuture<Void> allOf(Collection<CompletableFuture<? extends T>> futuresList) {
		return CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));
	}

	static <T> FutureResult<T> getSafeResult(@Nonnull CompletableFuture<T> future) {
		if (isFailed(future)) {
			MutableObject<Throwable> throwableMutableObject = new MutableObject<>();
			future.exceptionally(throwable -> {
				throwableMutableObject.setValue(throwable);
				return null;
			});
			return new FutureResult<>(null, throwableMutableObject.getValue());
		}
		return new FutureResult<>(future.join(), null);
	}

	static boolean isFailed(@Nonnull CompletableFuture<?> future) {
		return future.isCompletedExceptionally() || future.isCancelled();
	}

	/**
	 * Returns a new CompletableFuture that is already exceptionally
	 * completed with the given throwable.
	 *
	 * @param throwable the throwable
	 * @param <U>       the type of the value
	 * @return the completed CompletableFuture
	 * @see CompletableFuture#completedFuture(Object)
	 */
	static <U> CompletableFuture<U> completedExceptionally(Throwable throwable) {
		CompletableFuture<U> completableFuture = new CompletableFuture<>();
		completableFuture.completeExceptionally(throwable);
		return completableFuture;
	}

	/**
	 * Awaits for the future's completion either successfully or exceptionally,
	 * suppressing <strong>ALL</strong> the thrown checked exceptions.
	 * <p>This is typically used in tests with asynchronous methods.
	 *
	 * @param futures the futures to await for
	 */
	static void awaitQuietly(CompletableFuture<?>... futures) {
		awaitQuietly(Arrays.asList(futures));
	}

	/**
	 * Awaits for the future's completion either successfully or exceptionally,
	 * suppressing <strong>ALL</strong> the thrown checked exceptions.
	 * <p>This is typically used in tests with asynchronous methods.
	 *
	 * @param futures the futures to await for
	 */
	static void awaitQuietly(Collection<CompletableFuture<?>> futures) {
		try {
			awaitExecution(futures);
		} catch (ExecutionException | CancellationException exception) {
			log.info("An exception was suppressed while awaiting to futures to complete: "
				+ exception.getMessage());
		}
	}

	/**
	 * Unsafe method that awaits for the future's completion either successfully
	 * or exceptionally.
	 *
	 * @param futures the futures to await for
	 * @throws ExecutionRuntimeException if any of the futures completed exceptionally
	 */
	static void await(CompletableFuture<?>... futures) {
		await(Arrays.asList(futures));
	}

	/**
	 * Unsafe method that awaits for the future's completion either successfully
	 * or exceptionally.
	 *
	 * @param futures the futures to await for
	 * @throws ExecutionRuntimeException if any of the futures completed exceptionally
	 */
	static void await(Collection<CompletableFuture<?>> futures) {
		try {
			awaitExecution(futures);
		} catch (ExecutionException e) {
			throw new ExecutionRuntimeException(e.getCause());
		} catch (CancellationException c) {
			throw new ExecutionRuntimeException(c);
		}
	}

	private static void awaitExecution(Collection<CompletableFuture<?>> futuresList) throws ExecutionException {
		try {
			allOf(futuresList).get();
		} catch (InterruptedException e) {
			log.error(NzzFutures.class.getSimpleName() + "#awaitExecution() was interrupted: " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

}
