package br.com.nzz.validation.impl.di;

import br.com.nzz.validation.di.DIContainer;

/**
 * A {@code null} instance provider. For any class, a {@code null} will be returned.
 *
 * @author Luiz.Nazari
 */
public class NullDIContainer implements DIContainer {

	@Override
	public <T> boolean canProvide(Class<T> clazz) {
		return true;
	}

	@Override
	public <T> T provide(Class<T> clazz) {
		return null;
	}

}
