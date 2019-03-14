package br.com.nzz.commons;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NzzNumberUtils {

	private NzzNumberUtils() {
	}

	/**
	 * #.## or #,###,###.##
	 */
	private static final Pattern PATTERN_TEXT_NUMBER_EN = Pattern.compile("[\\D,.]*(\\d+(?:\\.\\d+)?|\\d+(?:(?:,\\d{3})+)\\.\\d+)[\\D,.]*");
	/**
	 * #,## or #.###.###,##
	 */
	private static final Pattern PATTERN_TEXT_NUMBER_PT_BR = Pattern.compile("[\\D,.]*(\\d+,\\d+|\\d+(?:(?:\\.\\d{3})+),\\d+)[\\D,.]*");
	private static final NumberFormat NUMBER_FORMAT_EN = NumberFormat.getNumberInstance(Locale.forLanguageTag("en"));
	private static final NumberFormat NUMBER_FORMAT_PT_BR = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));

	public static Optional<Double> extractNumber(String text) {
		try {
			return extractNumberFromText(text);
		} catch (ParseException e) {
			// Should never resolve here, the number format is already being validated before the parsing.
			throw new IllegalArgumentException("Could not extract number from string \"" + text + "\"", e);
		}
	}

	private static Optional<Double> extractNumberFromText(String text) throws ParseException {
		if (StringUtils.isNotEmpty(text)) {
			Matcher matcher;

			if ((matcher = PATTERN_TEXT_NUMBER_EN.matcher(text)).matches()) { // NOSONAR
				String number = matcher.group(1);
				return Optional.of(NUMBER_FORMAT_EN.parse(number).doubleValue());

			} else if ((matcher = PATTERN_TEXT_NUMBER_PT_BR.matcher(text)).matches()) { // NOSONAR
				String number = matcher.group(1);
				return Optional.of(NUMBER_FORMAT_PT_BR.parse(number).doubleValue());
			}
		}
		return Optional.empty();
	}

}
