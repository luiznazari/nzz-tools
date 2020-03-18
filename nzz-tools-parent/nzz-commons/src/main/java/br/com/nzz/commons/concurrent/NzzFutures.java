package br.com.nzz.commons.concurrent;

/**
 * @author Luiz Felipe Nazari
 */
public final class NzzFutures {

	private NzzFutures() {
	}

	public static <R, T extends Throwable> NzzCompletableFuture<R> resolve(UnsafeSupplier<R, T> supplier) {
		return new NzzCompletableFuture<>(supplier);
	}

}
