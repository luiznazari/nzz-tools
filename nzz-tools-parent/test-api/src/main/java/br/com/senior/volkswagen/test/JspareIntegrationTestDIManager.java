package br.com.senior.volkswagen.test;

import br.com.senior.volkswagen.injector.InjectMockStrategy;
import com.google.common.collect.Maps;
import org.apache.commons.lang.reflect.FieldUtils;
import org.jspare.core.ApplicationContext;
import org.jspare.core.Environment;
import org.jspare.core.InjectorAdapter;
import org.jspare.jpa.injector.RepositoryInjectStrategy;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * Dependency Injection manager for tests.
 *
 * @author Luiz.Nazari
 */
public abstract class JspareIntegrationTestDIManager {

	private static final String INJECTORS_FIELD = "INJECTORS";

	private static final Map<Class<?>, Object> injectedInstances = Maps.newHashMap();

	/**
	 * This answer adds some standard return possibilities for mocked classes methods, instead
	 * of returning null, it'll return empty objects or empty collections.
	 */
	private static final Answer<?> SMART_ANSWER =
		invocation -> {
			Class<?> returnType = invocation.getMethod().getReturnType();

			if (Optional.class.isAssignableFrom(returnType)) {
				return Optional.empty();

			} else if (List.class.isAssignableFrom(returnType)) {
				return Collections.emptyList();

			} else if (Set.class.isAssignableFrom(returnType)) {
				return Collections.emptySet();

			} else if (Page.class.isAssignableFrom(returnType)) {
				return Page.empty();
			}

			return null;
		};

	private JspareIntegrationTestDIManager() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getMockedInstance(Class<T> clazz) {
		if (injectedInstances.containsKey(clazz)) {
			return (T) injectedInstances.get(clazz);
		}

		Object mockedInstance = Mockito.mock(clazz, Mockito.withSettings().defaultAnswer(SMART_ANSWER));
		injectedInstances.put(clazz, mockedInstance);
		return (T) mockedInstance;
	}

	public static void resetMockedInstances() {
		MockUtil mockUtil = new MockUtil();
		injectedInstances.entrySet().stream()
			.filter(entry -> mockUtil.isMock(entry.getValue()))
			.forEach(entry -> Mockito.reset(entry.getValue()));
	}

	/**
	 * Replaces the original Jspare's injector for @{@link org.springframework.stereotype.Repository} classes
	 * with a custom injector for mocked repositories.<br>
	 * In other words: replaces the {@link RepositoryInjectStrategy} with {@link InjectMockStrategy}.
	 */
	static void registerMockInjectStrategy() {
		ApplicationContext applicationContext = Environment.getContext();

		removeJspareDefaultRepositoryInjectStrategy(applicationContext);

		InjectorAdapter repositoryMockInjector = new InjectMockStrategy();
		applicationContext.addInjector(repositoryMockInjector);
	}

	@SuppressWarnings("unchecked")
	private static void removeJspareDefaultRepositoryInjectStrategy(ApplicationContext applicationContext) {
		try {
			Map<Class<? extends InjectorAdapter>, InjectorAdapter> jspareInjectors = (Map<Class<? extends InjectorAdapter>, InjectorAdapter>)
				FieldUtils.readDeclaredField(applicationContext, INJECTORS_FIELD, true);
			jspareInjectors.remove(RepositoryInjectStrategy.class);
			jspareInjectors.remove(InjectMockStrategy.class);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	public static void initializeMocks(Object object) {
		InjectMockStrategy mockStrategy = new InjectMockStrategy();
		Arrays.stream(object.getClass().getDeclaredFields()).forEach(field -> {
			if (mockStrategy.isInjectable(field)) {
				mockStrategy.inject(object, field);
			}
		});

	}

}
