package br.com.nzz.spring.ws.adapter;

import br.com.nzz.commons.model.ValorDecimal;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author Luiz.Nazari
 */
public class ValorDecimalJsonSerializer extends StdSerializer<ValorDecimal> {

	public ValorDecimalJsonSerializer() {
		super(ValorDecimal.class);
	}

	@Override
	public void serialize(ValorDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.getValor().doubleValue());
	}

}
