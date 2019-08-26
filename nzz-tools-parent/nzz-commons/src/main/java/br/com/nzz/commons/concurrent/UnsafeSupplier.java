package br.com.nzz.commons.concurrent;

@FunctionalInterface
public interface UnsafeSupplier<T> {

	T get() throws Throwable;

}
