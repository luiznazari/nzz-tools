package br.com.nzz.commons;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public abstract class NzzNumberUtils {

	private NzzNumberUtils() {
	}

	private static final Pattern PATTERN_NOT_NUMBER = Pattern.compile("\\D+");

	/**
	 * #.## or #,###,###.##
	 */
	private static final Pattern PATTERN_NUMBER_EN = Pattern.compile("\\d+(?:\\.\\d+)?|\\d+(?:(?:,\\d{3})+)\\.\\d+");
	/**
	 * any text(#,## or #.###.###,##)any text
	 */
	private static final Pattern PATTERN_TEXT_NUMBER_EN = Pattern.compile("[\\D,.]*(\\d+(?:\\.\\d+)?|\\d+(?:(?:,\\d{3})+)\\.\\d+)[\\D,.]*");
	/**
	 * #,## or #.###.###,##
	 */
	private static final Pattern PATTERN_NUMBER_PT_BR = Pattern.compile("\\d+,\\d+|\\d+(?:(?:\\.\\d{3})+),\\d+");
	/**
	 * any text(#,## or #.###.###,##)any text
	 */
	private static final Pattern PATTERN_TEXT_NUMBER_PT_BR = Pattern.compile("[\\D,.]*(\\d+,\\d+|\\d+(?:(?:\\.\\d{3})+),\\d+)[\\D,.]*");

	/**
	 * Try to parse the string as Double. Matches ISO format (#.## or #,###,###.##)
	 * and brazilian format (#,## or #.###.###,##). To extract a number within a
	 * text or within a text with "dirty characters" use {@link NzzNumberUtils#extractNumber(String)}.
	 *
	 * @param text the text that may represents a valid number.
	 * @return an optional containing the number or empty if it fails to parse the text.
	 */
	public static Optional<Double> parseNumber(@Nullable String text) {
		try {
			return parseNumberFromText(text);
		} catch (ParseException e) {
			// Should never resolve here, the number format is already being validated before the parsing.
			throw new IllegalArgumentException("Could not parse number from string \"" + text + "\"", e);
		}
	}

	private static Optional<Double> parseNumberFromText(String text) throws ParseException {
		if (StringUtils.isNotEmpty(text)) {
			if (PATTERN_NUMBER_EN.matcher(text).matches()) {
				return Optional.of(getNumberFormatEn().parse(text).doubleValue());

			} else if (PATTERN_NUMBER_PT_BR.matcher(text).matches()) {
				return Optional.of(getNumberFormatPtBr().parse(text).doubleValue());
			}
		}
		return Optional.empty();
	}

	/**
	 * Try to extract a number from the string as Double. Matches ISO format (#.## or #,###,###.##)
	 * and brazilian format (#,## or #.###.###,##). To parse a represented as a string, use
	 * {@link NzzNumberUtils#parseNumber(String)}.
	 *
	 * @param text the text that may represents a valid number.
	 * @return an optional containing the number or empty if it fails to extract a number.
	 */
	public static Optional<Double> extractNumber(@Nullable String text) {
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

			if ((matcher = PATTERN_TEXT_NUMBER_EN.matcher(text)).matches()) {
				String number = matcher.group(1);
				return Optional.of(getNumberFormatEn().parse(number).doubleValue());

			} else if ((matcher = PATTERN_TEXT_NUMBER_PT_BR.matcher(text)).matches()) {
				String number = matcher.group(1);
				return Optional.of(getNumberFormatPtBr().parse(number).doubleValue());
			}
		}
		return Optional.empty();
	}

	private static NumberFormat getNumberFormatEn(){
		return NumberFormat.getNumberInstance(Locale.forLanguageTag("en"));
	}
	private static NumberFormat getNumberFormatPtBr(){
		return NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));
	}

	/**
	 * Remove all non-numeric characters from the text.
	 *
	 * @param text the text that may contain numeric characters.
	 * @return the string with only the numeric characters.
	 */
	public static String removeNonNumberCharacters(@Nullable String text) {
		if (text == null) {
			return StringUtils.EMPTY;
		}
		return PATTERN_NOT_NUMBER.matcher(text).replaceAll(StringUtils.EMPTY);
	}

	/**
	 * Validates if a string is a valid number.
	 *
	 * @param value the string that may represents a valid number;
	 * @return <code>true</code> if the string is a number,
	 * <code>false</code> otherwise.
	 */
	public static boolean isNumber(@Nullable String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		int sz = value.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validates if a string is a valid decimal number.
	 *
	 * @param value the string that may represents a valid number;
	 * @return <code>true</code> if the string is a decimal number,
	 * <code>false</code> otherwise.
	 */
	public static boolean isDecimalNumber(@Nullable String value) {
		if (value == null) {
			return false;
		}
		boolean isNumber = PATTERN_NUMBER_EN.matcher(value).find();
		if (!isNumber) {
			return false;
		}
		try {
			Double.valueOf(value);
			return true;
		} catch (NumberFormatException e) { // NOSONAR
			return false;
		}
	}

}
