package br.com.nzz.validation.message;

import br.com.nzz.validation.exception.ValidationApiException;
import br.com.nzz.validation.impl.NullSafeResourceBundle;
import br.com.nzz.validation.utils.StringUtils;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class I18n {

	private I18n() {
	}

	private static ResourceBundle VALIDATION_MESSAGES; // NOSONAR
	private static final Pattern PATTERN_PARAMETER = Pattern.compile("\\{(\\d*)}");

	static {
		VALIDATION_MESSAGES = new NullSafeResourceBundle("ValidationMessages");
	}

	public static String getMessage(String key, Object... parameters) {
		String i18nMessage = VALIDATION_MESSAGES.getString(key);
		if (parameters.length > 0) {
			return parseParameterizedMessage(i18nMessage, parameters);
		}
		return i18nMessage;
	}

	private static String parseParameterizedMessage(String i18nMessage, Object[] parameters) {
		StringBuilder sb = new StringBuilder(i18nMessage);

		int offset = 0;
		for (ParameterPlace parameterPlace : extractParameterHolders(i18nMessage)) {
			if (parameterPlace.index >= parameters.length) {
				throw new ValidationApiException(
					String.format("Error could not parse message \"%s\" with parameters: %s", i18nMessage, StringUtils.toString(parameters)));
			}

			String param = String.valueOf(parameters[parameterPlace.index]);
			sb.replace(parameterPlace.start + offset, parameterPlace.end + offset, param);
			offset += param.length() - parameterPlace.length();
		}

		return sb.toString();
	}

	private static List<ParameterPlace> extractParameterHolders(String i18nMessage) {
		List<ParameterPlace> parameterPlaces = Lists.newLinkedList();

		Matcher matcher = PATTERN_PARAMETER.matcher(i18nMessage);
		int index = 0;
		while (matcher.find()) {
			parameterPlaces.add(new ParameterPlace(index++, matcher));
		}

		return parameterPlaces;
	}

	private static class ParameterPlace {

		private final int end;
		private final int start;
		private final Integer index;

		private ParameterPlace(int index, Matcher matcher) {
			this.start = matcher.start();
			this.end = matcher.end();

			if (StringUtils.isNotEmpty(matcher.group(1))) {
				this.index = Integer.valueOf(matcher.group(1));
			} else {
				this.index = index;
			}
		}

		private int length() {
			return this.end - this.start;
		}

	}

}
