package br.com.nzz.commons;

import org.junit.Test;

import static br.com.nzz.commons.NzzNumberUtils.extractNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NzzNumberUtilsTest {

	@Test
	public void shouldExtractNumberFromText() {
		assertDouble(null, extractNumber(null).orElse(null));
		assertDouble(null, extractNumber("").orElse(null));
		assertDouble(null, extractNumber("   ").orElse(null));
		assertDouble(0.0, extractNumber("0").orElse(null));
		assertDouble(42.0, extractNumber("42").orElse(null));
		assertDouble(0.0, extractNumber("0.00").orElse(null));
		assertDouble(0.0, extractNumber("0,00").orElse(null));
		assertDouble(23.5, extractNumber("23,5").orElse(null));
		assertDouble(28.12, extractNumber("28.12 % t").orElse(null));
		assertDouble(1.43, extractNumber("1.43 Pts").orElse(null));
		assertDouble(61.40, extractNumber("61.40 % t").orElse(null));
		assertDouble(26.5, extractNumber("26,5").orElse(null));
		assertDouble(3.47, extractNumber("3,47 % t").orElse(null));
		assertDouble(24.60, extractNumber("Körperhaltung24.60 Pontos").orElse(null));
		assertDouble(7.10, extractNumber("Lastenhandhabung7.10 Pts").orElse(null));
		assertDouble(25.20, extractNumber("Körperhaltung 25.20 Pts").orElse(null));
		assertDouble(9999999.99, extractNumber("9,999,999.99").orElse(null));
		assertDouble(99999999.99, extractNumber("99.999.999,99").orElse(null));
	}

	private void assertDouble(Double expected, Double actual) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected, actual, org.apache.commons.lang3.math.NumberUtils.DOUBLE_ZERO);
		}
	}

}