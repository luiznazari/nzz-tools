package br.com.nzz.test.injector;

import br.com.nzz.test.JspareIntegrationTestDIManager;
import org.jspare.core.InjectorAdapter;
import org.jspare.jpa.annotation.RepositoryInject;
import org.jspare.unit.mock.Mock;
import org.junit.Assert;

import java.lang.reflect.Field;

/**
 * Injector for instantiating mocks inside integration tests.
 *
 * @author Luiz.Nazari
 */
public class InjectMockStrategy implements InjectorAdapter {

	@Override
	public boolean isInjectable(Field field) {
		return field.isAnnotationPresent(RepositoryInject.class) || field.isAnnotationPresent(Mock.class);
	}

	@Override
	public void inject(Object instance, Field field) {
		Object mockedInstance = JspareIntegrationTestDIManager.getMockedInstance(field.getType());

		try {
			field.setAccessible(true);
			field.set(instance, mockedInstance);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

}