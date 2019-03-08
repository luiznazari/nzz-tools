package br.com.nzz.validation.impl.di;

import br.com.nzz.validation.di.DIContainer;
import br.com.nzz.validation.exception.ValidationApiException;
import org.jspare.core.Component;
import org.jspare.core.Environment;

/**
 * The Vert.x Jspare Dependency Injection container.
 *
 * @author Luiz.Nazari
 */
public class VertxJspareDIContainer implements DIContainer {

	private static final String TEMPLATE_ERROR_JSPARE_NOT_LOADED = "Failed to get a provided instance of \"%s\"." +
		" Vert.x Jspare context is not loaded.";

	@Override
	public <T> boolean canProvide(Class<T> clazz) {
		return clazz.isAnnotationPresent(Component.class);
	}

	@Override
	public <T> T provide(Class<T> clazz) {
		if (!Environment.isLoaded()) {
			throw new ValidationApiException(String.format(TEMPLATE_ERROR_JSPARE_NOT_LOADED, clazz.getName()));
		}
		return Environment.provide(clazz);
	}

}
