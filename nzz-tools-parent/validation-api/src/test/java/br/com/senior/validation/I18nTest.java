package br.com.senior.validation;

import br.com.senior.test.UnitTest;
import br.com.senior.validation.message.I18n;
import com.google.common.collect.Maps;
import org.junit.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

public class I18nTest extends UnitTest {

	private static ResourceBundle i18nOriginalResourceBundle;
	private static I18nTestResourceBundle i18nMessages;

	@BeforeClass
	public static void initializeI18nResourceBundle() throws NoSuchFieldException, IllegalAccessException {
		I18nTest.i18nMessages = new I18nTestResourceBundle();

		final String RESOURCE_BUNDLE_FIELD = "VALIDATION_MESSAGES";
		Field i18nResourceBundleField = I18n.class.getDeclaredField(RESOURCE_BUNDLE_FIELD);
		i18nResourceBundleField.setAccessible(true);
		I18nTest.i18nOriginalResourceBundle = (ResourceBundle) i18nResourceBundleField.get(I18n.class);
		i18nResourceBundleField.set(I18n.class, I18nTest.i18nMessages);
	}

	@AfterClass
	public static void restoreI18nResourceBundle() throws NoSuchFieldException, IllegalAccessException {
		final String RESOURCE_BUNDLE_FIELD = "VALIDATION_MESSAGES";
		Field i18nResourceBundleField = I18n.class.getDeclaredField(RESOURCE_BUNDLE_FIELD);
		i18nResourceBundleField.setAccessible(true);
		i18nResourceBundleField.set(I18n.class, I18nTest.i18nOriginalResourceBundle);
		I18nTest.i18nOriginalResourceBundle = null;
	}

	@After
	public void cleanTestMessages() {
		i18nMessages.messages.clear();
	}

	@Test
	public void shouldParseMessageTemplateWithEnumeratedParameters() {
		String key = "test.message.with.parameters";
		String message = "This is a {0} message, with {1} parameters.";
		i18nMessages.put(key, message);

		String parsedMessage = I18n.getMessage(key, "beautiful", 2);
		Assert.assertEquals("This is a beautiful message, with 2 parameters.", parsedMessage);
	}

	@Test
	public void shouldParseMessageTemplateWithEnumeratedParameters2() {
		String key = "error.could.not.calculate.risk";
		String message = "O risco \"{0}\" não pode ser calculado para o posto de trabalho {1}. Motivo: a métrica \"{2}\" não foi encontrada";
		i18nMessages.put(key, message);

		String parsedMessage = I18n.getMessage(key, "Cair da cama", "CM402", "Altura do colchão");
		Assert.assertEquals("O risco \"Cair da cama\" não pode ser calculado para o posto de trabalho CM402. Motivo: a métrica \"Altura do colchão\" não foi encontrada",
			parsedMessage);
	}

	@Test
	public void shouldParseMessageTemplateWithParameters() {
		String key = "error.could.not.calculate.risk";
		String message = "O risco \"{}\" não pode ser calculado para o posto de trabalho {}. Motivo: a métrica \"{}\" não foi encontrada";
		i18nMessages.put(key, message);

		String parsedMessage = I18n.getMessage(key, "Cair da cama", "CM402", "Altura do colchão");
		Assert.assertEquals("O risco \"Cair da cama\" não pode ser calculado para o posto de trabalho CM402. Motivo: a métrica \"Altura do colchão\" não foi encontrada",
			parsedMessage);
	}

	static class I18nTestResourceBundle extends ResourceBundle {

		private static final String TEMPLATE_UNKOWN_KEY = "[%s]";

		private final Map<String, String> messages = Maps.newHashMap();

		@Override
		protected Object handleGetObject(@Nonnull String key) {
			if (this.messages.containsKey(key)) {
				return this.messages.get(key);
			}
			return String.format(TEMPLATE_UNKOWN_KEY, key);
		}

		@Nonnull
		@Override
		public Enumeration<String> getKeys() {
			return Collections.emptyEnumeration();
		}

		I18nTestResourceBundle put(String key, String message) {
			this.messages.put(key, message);
			return this;
		}

	}

}