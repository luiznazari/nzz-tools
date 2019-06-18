package br.com.nzz.commons.concurrent;

import java.util.function.Supplier;

/**
 * @author Luiz Felipe Nazari
 */
public final class NzzFutures {

	private NzzFutures() {
	}

	public static <R> NzzCompletableFuture<R> resolve(Supplier<R> supplier) {
		return new NzzCompletableFuture<>(supplier);
	}

}
