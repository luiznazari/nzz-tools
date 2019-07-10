package br.com.nzz.spring.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

import javax.xml.datatype.XMLGregorianCalendar;

import br.com.nzz.spring.utils.XmlGregorianCalendarUtils;

/**
 * @author Luiz.Nazari
 */
public class XMLGregorianCalendarJsonSerializer extends StdSerializer<XMLGregorianCalendar> {

	public XMLGregorianCalendarJsonSerializer() {
		super(XMLGregorianCalendar.class);
	}

	@Override
	public void serialize(XMLGregorianCalendar value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(XmlGregorianCalendarUtils.format(value));
	}

}
