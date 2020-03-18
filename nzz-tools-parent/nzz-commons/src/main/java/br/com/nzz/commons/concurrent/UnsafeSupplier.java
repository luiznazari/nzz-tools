package br.com.nzz.commons.concurrent;

@FunctionalInterface
public interface UnsafeSupplier<R, T extends Throwable> {

	R get() throws T;

}

