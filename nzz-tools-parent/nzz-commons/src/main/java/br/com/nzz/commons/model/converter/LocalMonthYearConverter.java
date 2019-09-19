package br.com.nzz.commons.model.converter;

import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.com.nzz.commons.model.LocalMonthYear;

/**
 * @author Luiz.Nazari
 */
@Converter(autoApply = true)
public class LocalMonthYearConverter implements AttributeConverter<LocalMonthYear, String> {

	@Override
	public String convertToDatabaseColumn(LocalMonthYear attribute) {
		return attribute.getValue().format(DateTimeFormatter.ISO_DATE);
	}

	@Override
	public LocalMonthYear convertToEntityAttribute(String dbData) {
		return LocalMonthYear.parse(dbData);
	}

}