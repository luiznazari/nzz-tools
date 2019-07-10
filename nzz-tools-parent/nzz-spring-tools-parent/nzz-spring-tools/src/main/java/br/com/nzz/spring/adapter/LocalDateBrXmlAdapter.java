package br.com.nzz.spring.adapter;

import com.migesok.jaxb.adapter.javatime.TemporalAccessorXmlAdapter;

import java.time.LocalDate;

import br.com.nzz.commons.NzzDateUtils;

/**
 * {@code XmlAdapter} mapping JSR-310 {@code LocalDate} to Brazilian date string, time-zone information ignored.
 *
 * @see javax.xml.bind.annotation.adapters.XmlAdapter
 * @see LocalDate
 */
public class LocalDateBrXmlAdapter extends TemporalAccessorXmlAdapter<LocalDate> {

	public LocalDateBrXmlAdapter() {
		super(NzzDateUtils.BR_DATE_FORMATTER, LocalDate::from);
	}
}
