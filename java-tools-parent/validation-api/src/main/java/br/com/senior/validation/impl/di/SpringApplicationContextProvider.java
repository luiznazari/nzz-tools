package br.com.senior.validation.impl.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("applicationContextProvider")
public class SpringApplicationContextProvider implements ApplicationContextAware {

	public static ApplicationContext getApplicationContext() {
		return ApplicationContextHolder.CONTEXT_PROVIDER.context;
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) {
		ApplicationContextHolder.CONTEXT_PROVIDER.setContext(ac);
	}

	private static class ApplicationContextHolder {

		private static final InnerContextResource CONTEXT_PROVIDER = new InnerContextResource();

	}

	private static final class InnerContextResource {

		private ApplicationContext context;

		private void setContext(ApplicationContext context) {
			this.context = context;
		}

	}

}