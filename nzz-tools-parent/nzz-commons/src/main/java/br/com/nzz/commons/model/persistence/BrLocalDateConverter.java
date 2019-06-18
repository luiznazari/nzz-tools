package br.com.nzz.commons.model.persistence;

import br.com.nzz.commons.NzzDateUtils;
import br.com.nzz.commons.model.BrLocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

@Converter(autoApply = true)
public class BrLocalDateConverter implements AttributeConverter<BrLocalDate, String> {

	@Override
	public String convertToDatabaseColumn(BrLocalDate attribute) {
		return NzzDateUtils.BR_DATE_FORMATTER.format(attribute.getValue());
	}

	@Override
	public BrLocalDate convertToEntityAttribute(String dbData) {
		TemporalAccessor temporalAccessor = NzzDateUtils.BR_DATE_FORMATTER.parse(dbData);
		LocalDate value = LocalDate.from(temporalAccessor);
		return BrLocalDate.from(value);
	}

}
