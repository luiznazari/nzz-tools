package br.com.nzz.commons.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Simple object that holds either the value or the throwable of a
 * {@link CompletableFuture}. The value will be present if the {@link CompletableFuture}
 * completed successfully and the throwable will be present if
 * it has completed exceptionally, never both.
 *
 * @param <T> the type of the {@link CompletableFuture}'s value
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("WeakerAccess") // public lib
public class FutureResult<T> {

	/**
	 * The successful {@link CompletableFuture}'s original value.
	 */
	private final T result;
	/**
	 * The failed {@link CompletableFuture}'s original throwable.
	 */
	private final Throwable throwable;

	/**
	 * Indicate that the original {@link CompletableFuture} had completed
	 * successfully, thus, containing a {@link FutureResult#result}. The
	 * result may still be <code>null</code> if the original future returned
	 * a <code>null</code> value.
	 *
	 * @return <code>true</code> if the original {@link CompletableFuture}
	 * was completed successfully, <code>false</code> otherwise.
	 */
	public boolean isSuccessful() {
		return this.throwable == null;
	}

	/**
	 * If a result is present, invoke the specified consumer with the result,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if a result is present
	 * @return this
	 */
	public FutureResult ifSucceed(@Nonnull Consumer<T> consumer) {
		if (this.isSuccessful()) {
			consumer.accept(this.result);
		}
		return this;
	}

	/**
	 * If a throwable is present, invoke the specified consumer with the throwable,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if a throwable is present
	 * @return this
	 */
	public FutureResult ifFailed(@Nonnull Consumer<Throwable> consumer) {
		if (!this.isSuccessful()) {
			consumer.accept(this.throwable);
		}
		return this;
	}

}