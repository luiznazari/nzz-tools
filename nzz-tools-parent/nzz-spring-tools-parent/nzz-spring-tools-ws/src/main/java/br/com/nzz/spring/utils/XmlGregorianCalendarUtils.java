package br.com.nzz.spring.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import br.com.nzz.commons.NzzDateUtils;
import br.com.nzz.spring.exception.WebServiceInternalException;
import lombok.NonNull;

/**
 * <pre>
 *  (GMT-3:00) - BET - America/Sao_Paulo
 *  (GMT-5:00) Brazil/Acre
 *  (GMT-2:00) Brazil/DeNoronha
 *  (GMT-3:00) Brazil/East
 *  (GMT-4:00) Brazil/West
 * </pre>
 */
public class XmlGregorianCalendarUtils {

	private static ZoneId DEFAULT_ZONE_ID = NzzDateUtils.BR_ZONE_ID;

	private XmlGregorianCalendarUtils() {
	}

	public static void setDefaultZoneId(@NonNull ZoneId zoneId) {
		XmlGregorianCalendarUtils.DEFAULT_ZONE_ID = zoneId;
	}

	public static LocalDate toLocalDate(XMLGregorianCalendar xmlCalendar) {
		return xmlCalendar.toGregorianCalendar().toZonedDateTime().withZoneSameLocal(DEFAULT_ZONE_ID).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlCalendar) {
		return xmlCalendar.toGregorianCalendar().toZonedDateTime().withZoneSameLocal(DEFAULT_ZONE_ID).toLocalDateTime();
	}

	public static XMLGregorianCalendar toXgc(LocalDate localDate) {
		return toXgc(localDate.atStartOfDay());
	}

	public static XMLGregorianCalendar toXgc(LocalDateTime localDateTime) {
		return toXgc(localDateTime, DEFAULT_ZONE_ID);
	}

	public static XMLGregorianCalendar toXgc(LocalDateTime localDateTime, ZoneId zoneId) {
		GregorianCalendar calendar = GregorianCalendar.from(localDateTime.atZone(zoneId));
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			throw new WebServiceInternalException("Could not convert to XmlGregorianCalendar.", e).runtime();
		}
	}

	public static String format(XMLGregorianCalendar xmlCalendar) {
		return xmlCalendar.toXMLFormat();
	}

}
