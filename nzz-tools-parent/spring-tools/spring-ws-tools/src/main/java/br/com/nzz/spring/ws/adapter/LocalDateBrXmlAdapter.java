package br.com.nzz.spring.ws.adapter;

import br.com.nzz.commons.NzzDateUtils;
import com.migesok.jaxb.adapter.javatime.TemporalAccessorXmlAdapter;

import java.time.LocalDate;

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
