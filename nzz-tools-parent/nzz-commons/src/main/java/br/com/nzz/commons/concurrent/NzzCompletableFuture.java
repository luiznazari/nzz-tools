package br.com.nzz.commons.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <R> The result type returned by this Future's {@code get} method
 * @author Luiz Felipe Nazari
 */
public class NzzCompletableFuture<R> implements Future<R>, CompletionStage<R> {

	private final UnsafeSupplier<R, ? extends Throwable> completableSupplier;
	private final CompletableFuture<R> delegate;

	NzzCompletableFuture(UnsafeSupplier<R, ? extends Throwable> supplier) {
		this.completableSupplier = supplier;
		this.delegate = new CompletableFuture<>();
		executeAsynchronously();
	}

	private void executeAsynchronously() {
		new Thread(() -> {
			try {
				this.delegate.complete(this.completableSupplier.get());
			} catch (Throwable throwable) {
				this.delegate.completeExceptionally(throwable);
			}
		}).start();
	}

	// =-=-=-=-=-=-=-=-=-=: Delegates :=-=-=-=-=-=-=-=-=-=

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return this.delegate.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return this.delegate.isCancelled();
	}

	@Override
	public boolean isDone() {
		return this.delegate.isDone();
	}

	@Override
	public R get() throws InterruptedException, ExecutionException {
		return this.delegate.get();
	}

	@Override
	public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return this.delegate.get(timeout, unit);
	}

	@Override
	public <U> CompletionStage<U> thenApply(Function<? super R, ? extends U> fn) {
		return this.delegate.thenApply(fn);
	}

	@Override
	public <U> CompletionStage<U> thenApplyAsync(Function<? super R, ? extends U> fn) {
		return this.delegate.thenApplyAsync(fn);
	}

	@Override
	public <U> CompletionStage<U> thenApplyAsync(Function<? super R, ? extends U> fn, java.util.concurrent.Executor executor) {
		return this.delegate.thenApplyAsync(fn, executor);
	}

	@Override
	public CompletionStage<Void> thenAccept(Consumer<? super R> action) {
		return this.delegate.thenAccept(action);
	}

	@Override
	public CompletionStage<Void> thenAcceptAsync(Consumer<? super R> action) {
		return this.delegate.thenAcceptAsync(action);
	}

	@Override
	public CompletionStage<Void> thenAcceptAsync(Consumer<? super R> action, java.util.concurrent.Executor executor) {
		return this.delegate.thenAcceptAsync(action, executor);
	}

	@Override
	public CompletionStage<Void> thenRun(Runnable action) {
		return this.delegate.thenRun(action);
	}

	@Override
	public CompletionStage<Void> thenRunAsync(Runnable action) {
		return this.delegate.thenRunAsync(action);
	}

	@Override
	public CompletionStage<Void> thenRunAsync(Runnable action, java.util.concurrent.Executor executor) {
		return this.delegate.thenRunAsync(action, executor);
	}

	@Override
	public <U, V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super R, ? super U, ? extends V> fn) {
		return this.delegate.thenCombine(other, fn);
	}

	@Override
	public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super R, ? super U, ? extends V> fn) {
		return this.delegate.thenCombineAsync(other, fn);
	}

	@Override
	public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super R, ? super U, ? extends V> fn, java.util.concurrent.Executor executor) {
		return this.delegate.thenCombineAsync(other, fn);
	}

	@Override
	public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super R, ? super U> action) {
		return this.delegate.thenAcceptBoth(other, action);
	}

	@Override
	public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super R, ? super U> action) {
		return this.delegate.thenAcceptBothAsync(other, action);
	}

	@Override
	public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super R, ? super U> action, java.util.concurrent.Executor executor) {
		return this.delegate.thenAcceptBothAsync(other, action, executor);
	}

	@Override
	public CompletionStage<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
		return this.delegate.runAfterBoth(other, action);
	}

	@Override
	public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
		return this.delegate.runAfterBothAsync(other, action);
	}

	@Override
	public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, java.util.concurrent.Executor executor) {
		return this.delegate.runAfterBothAsync(other, action, executor);
	}

	@Override
	public <U> CompletionStage<U> applyToEither(CompletionStage<? extends R> other, Function<? super R, U> fn) {
		return this.delegate.applyToEither(other, fn);
	}

	@Override
	public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends R> other, Function<? super R, U> fn) {
		return this.delegate.applyToEitherAsync(other, fn);
	}

	@Override
	public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends R> other, Function<? super R, U> fn, java.util.concurrent.Executor executor) {
		return this.delegate.applyToEitherAsync(other, fn, executor);
	}

	@Override
	public CompletionStage<Void> acceptEither(CompletionStage<? extends R> other, Consumer<? super R> action) {
		return this.delegate.acceptEither(other, action);
	}

	@Override
	public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends R> other, Consumer<? super R> action) {
		return this.delegate.acceptEitherAsync(other, action);
	}

	@Override
	public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends R> other, Consumer<? super R> action, java.util.concurrent.Executor executor) {
		return this.delegate.acceptEitherAsync(other, action, executor);
	}

	@Override
	public CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
		return this.delegate.runAfterEither(other, action);
	}

	@Override
	public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
		return this.delegate.runAfterEitherAsync(other, action);
	}

	@Override
	public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, java.util.concurrent.Executor executor) {
		return this.delegate.runAfterEitherAsync(other, action, executor);
	}

	@Override
	public <U> CompletionStage<U> thenCompose(Function<? super R, ? extends CompletionStage<U>> fn) {
		return this.delegate.thenCompose(fn);
	}

	@Override
	public <U> CompletionStage<U> thenComposeAsync(Function<? super R, ? extends CompletionStage<U>> fn) {
		return this.delegate.thenComposeAsync(fn);
	}

	@Override
	public <U> CompletionStage<U> thenComposeAsync(Function<? super R, ? extends CompletionStage<U>> fn, java.util.concurrent.Executor executor) {
		return this.delegate.thenComposeAsync(fn, executor);
	}

	@Override
	public CompletionStage<R> exceptionally(Function<Throwable, ? extends R> fn) {
		return this.delegate.exceptionally(fn);
	}

	@Override
	public CompletionStage<R> whenComplete(BiConsumer<? super R, ? super Throwable> action) {
		return this.delegate.whenComplete(action);
	}

	@Override
	public CompletionStage<R> whenCompleteAsync(BiConsumer<? super R, ? super Throwable> action) {
		return this.delegate.whenCompleteAsync(action);
	}

	@Override
	public CompletionStage<R> whenCompleteAsync(BiConsumer<? super R, ? super Throwable> action, java.util.concurrent.Executor executor) {
		return this.delegate.whenCompleteAsync(action, executor);
	}

	@Override
	public <U> CompletionStage<U> handle(BiFunction<? super R, Throwable, ? extends U> fn) {
		return this.delegate.handle(fn);
	}

	@Override
	public <U> CompletionStage<U> handleAsync(BiFunction<? super R, Throwable, ? extends U> fn) {
		return this.delegate.handleAsync(fn);
	}

	@Override
	public <U> CompletionStage<U> handleAsync(BiFunction<? super R, Throwable, ? extends U> fn, java.util.concurrent.Executor executor) {
		return this.delegate.handleAsync(fn, executor);
	}

	@Override
	public CompletableFuture<R> toCompletableFuture() {
		return this.delegate.toCompletableFuture();
	}

}
