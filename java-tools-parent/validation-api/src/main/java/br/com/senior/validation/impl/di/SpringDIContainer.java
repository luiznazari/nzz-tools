package br.com.senior.validation.impl.di;

import br.com.senior.validation.di.DIContainer;
import br.com.senior.validation.exception.ValidationApiException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * The Spring Framework Dependency Injection container.
 *
 * @author Luiz.Nazari
 */
public class SpringDIContainer implements DIContainer {

	private static final String TEMPLATE_ERROR_SPRING_NOT_LOADED = " Failed to get a provided instance of \"%s\"." +
		" Spring context is not loaded or br.com.senior.validation.impl.di.ApplicationContextProvider is not a Spring Bean.";

	@Override
	public <T> boolean canProvide(Class<T> clazz) {
		return clazz.isAnnotationPresent(Service.class)
			|| clazz.isAnnotationPresent(Component.class)
			|| clazz.isAnnotationPresent(Controller.class)
			|| clazz.isAnnotationPresent(Repository.class);
	}

	@Override
	public <T> T provide(Class<T> clazz) {
		ApplicationContext applicationContext = SpringApplicationContextProvider.getApplicationContext();
		if (applicationContext != null) {
			return applicationContext.getBean(clazz);
		}

		throw new ValidationApiException(String.format(TEMPLATE_ERROR_SPRING_NOT_LOADED, clazz.getName()));
	}

}
