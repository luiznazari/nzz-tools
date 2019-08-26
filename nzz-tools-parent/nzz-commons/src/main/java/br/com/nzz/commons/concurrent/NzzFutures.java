package br.com.nzz.commons.concurrent;

/**
 * @author Luiz Felipe Nazari
 */
public final class NzzFutures {

	private NzzFutures() {
	}

	public static <R> NzzCompletableFuture<R> resolve(UnsafeSupplier<R> supplier) {
		return new NzzCompletableFuture<>(supplier);
	}

}
