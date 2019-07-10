package br.com.nzz.spring.adapter;

import com.migesok.jaxb.adapter.javatime.TemporalAccessorXmlAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.nzz.commons.NzzDateUtils;

/**
 * {@code XmlAdapter} mapping JSR-310 {@code LocalDateTime} to Brazilian date time string
 * <p>
 * String format details: {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}
 *
 * @see javax.xml.bind.annotation.adapters.XmlAdapter
 * @see LocalDateTime
 */
public class LocalDateTimeBrXmlAdapter extends TemporalAccessorXmlAdapter<LocalDateTime> {

	public LocalDateTimeBrXmlAdapter() {
		super(NzzDateUtils.BR_DATE_TIME_FORMATTER, LocalDateTime::from);
	}
}
