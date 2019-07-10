package br.com.nzz.commons.model.persistence;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.com.nzz.commons.NzzDateUtils;
import br.com.nzz.commons.model.LocalDateBr;

@Converter(autoApply = true)
public class LocalDateBrConverter implements AttributeConverter<LocalDateBr, String> {

	@Override
	public String convertToDatabaseColumn(LocalDateBr attribute) {
		return NzzDateUtils.BR_DATE_FORMATTER.format(attribute.getValue());
	}

	@Override
	public LocalDateBr convertToEntityAttribute(String dbData) {
		TemporalAccessor temporalAccessor = NzzDateUtils.BR_DATE_FORMATTER.parse(dbData);
		LocalDate value = LocalDate.from(temporalAccessor);
		return LocalDateBr.from(value);
	}

}
