package br.com.senior.validation.impl.di;

import br.com.senior.validation.di.DIContainer;

import java.util.Arrays;

/**
 * @author Luiz.Nazari
 */
public class DefaultConstructorDIContainer implements DIContainer {

	@Override
	public <T> boolean canProvide(Class<T> clazz) {
		return this.hasDefaultConstructor(clazz);
	}

	@Override
	public <T> T provide(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	private boolean hasDefaultConstructor(Class<?> clazz) {
		return Arrays.stream(clazz.getConstructors())
			.anyMatch(constructor -> constructor.getParameterCount() == 0);
	}

}
