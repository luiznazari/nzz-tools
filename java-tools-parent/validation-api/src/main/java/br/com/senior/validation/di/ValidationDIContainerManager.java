package br.com.senior.validation.di;

import br.com.senior.validation.exception.ValidationApiException;
import br.com.senior.validation.impl.di.DefaultConstructorDIContainer;
import br.com.senior.validation.impl.di.NullDIContainer;
import br.com.senior.validation.impl.di.SpringDIContainer;
import br.com.senior.validation.impl.di.VertxJspareDIContainer;
import com.google.common.collect.Maps;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * @author Luiz.Nazari
 */
public class ValidationDIContainerManager {

	private static final String TEMPLATE_ERROR_INSTANTIATE_CONTAINER = "Could not instantiate dependency injection container of class \"%s\". " +
		"Check if the class has an public constructor with zero arguments.";

	private static ValidationDIContainerManager instance;

	private final Deque<DIContainer> knownDIContainers;

	private ValidationDIContainerManager() {
		this.knownDIContainers = new ArrayDeque<>(5);
		this.registerDIContainer(new DefaultConstructorDIContainer());

		this.registerDIContainersInClasspath();
	}

	private void registerDIContainersInClasspath() {
		Map<String, Class<? extends DIContainer>> implementedDIContainers = Maps.newHashMap();
		implementedDIContainers.put("org.jspare.core.ApplicationContext", VertxJspareDIContainer.class);
		implementedDIContainers.put("org.springframework.context.ApplicationContext", SpringDIContainer.class);

		implementedDIContainers.forEach((referenceClassName, diContainerClass) -> {
			if (this.isClassInClasspath(referenceClassName)) {
				try {
					DIContainer diContainer = diContainerClass.newInstance();
					this.registerDIContainer(diContainer);
				} catch (InstantiationException | IllegalAccessException e) {
					String errorMessage = String.format(TEMPLATE_ERROR_INSTANTIATE_CONTAINER, diContainerClass.getName());
					throw new ValidationApiException(errorMessage);
				}
			}
		});
	}

	private boolean isClassInClasspath(String className) {
		try {
			return Class.forName(className) != null;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public void registerDIContainer(DIContainer diContainer) {
		this.knownDIContainers.add(diContainer);
	}

	public DIContainer getDIContainerFor(Class<?> clazz) {
		return this.knownDIContainers.stream()
			.filter(diContainer -> diContainer.canProvide(clazz))
			.findFirst()
			.orElseGet(NullDIContainer::new);
	}

	public static ValidationDIContainerManager getInstance() {
		if (ValidationDIContainerManager.instance == null) {
			ValidationDIContainerManager.instance = new ValidationDIContainerManager();
		}
		return ValidationDIContainerManager.instance;
	}

}
