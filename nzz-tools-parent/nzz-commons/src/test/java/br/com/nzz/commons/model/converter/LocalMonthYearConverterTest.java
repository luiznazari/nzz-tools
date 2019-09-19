package br.com.nzz.commons.model.converter;

import org.junit.Before;
import org.junit.Test;

import br.com.nzz.commons.model.LocalMonthYear;

import static org.junit.Assert.assertEquals;

public class LocalMonthYearConverterTest {

	private LocalMonthYearConverter localMonthYearConverter;

	@Before
	public void init() {
		localMonthYearConverter = new LocalMonthYearConverter();
	}

	@Test
	public void shouldConvertToString() {
		LocalMonthYear localMonthYear = LocalMonthYear.of(1994, 11);
		String monthYearString = localMonthYearConverter.convertToDatabaseColumn(localMonthYear);
		assertEquals("1994-11-01", monthYearString);
	}

	@Test
	public void shouldConvertToLocalMonthYear() {
		final String monthYearString = "1994-11-01";
		final LocalMonthYear expectedLocalMonthYear = LocalMonthYear.of(1994, 11);
		LocalMonthYear convertedMonthYear = localMonthYearConverter.convertToEntityAttribute(monthYearString);
		assertEquals(expectedLocalMonthYear, convertedMonthYear);
	}

	@Test
	public void shouldConvertCompatibleValues() {
		LocalMonthYear localMonthYear = LocalMonthYear.of(1994, 11);
		String monthYearString = localMonthYearConverter.convertToDatabaseColumn(localMonthYear);
		LocalMonthYear convertedMonthYear = localMonthYearConverter.convertToEntityAttribute(monthYearString);
		assertEquals(localMonthYear, convertedMonthYear);
	}

}