package br.com.nzz.spring.utils;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;

import static org.junit.Assert.assertEquals;

public class XmlGregorianCalendarUtilsTest {

	@Test
	public void shouldFormatToString() {
		XMLGregorianCalendar xgc = new XMLGregorianCalendarImpl();
		xgc.setDay(28);
		xgc.setMonth(11);
		xgc.setYear(1994);
		xgc.setTime(10, 12, 14);

		String formattedXgc = XmlGregorianCalendarUtils.format(xgc);
		assertEquals("1994-11-28T10:12:14", formattedXgc);
	}

	@Test
	public void shouldFormatToStringWithTimezone() {
		XMLGregorianCalendar xgc = new XMLGregorianCalendarImpl();
		xgc.setDay(28);
		xgc.setMonth(11);
		xgc.setYear(1994);
		xgc.setTime(10, 12, 14, 42);
		xgc.setTimezone(-3 * 60);

		String formattedXgc = XmlGregorianCalendarUtils.format(xgc);
		assertEquals("1994-11-28T10:12:14.042-03:00", formattedXgc);
	}

}