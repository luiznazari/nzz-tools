package br.com.nzz.spring.ws.adapter;

import com.fasterxml.jackson.databind.JsonSerializer;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class JacksonLoader extends NzzLoader {

	public static List<JsonSerializer<?>> loadSerializersFromClassPath() {
		List<JsonSerializer<?>> serializers = new ArrayList<>();
		serializers.add(new XMLGregorianCalendarJsonSerializer());
		if (AdapterLoader.isNzzCommonsEnabled()) {
			serializers.add(instantiateClass("br.com.nzz.spring.ws.adapter.ValorDecimalJsonSerializer"));
		}
		return serializers;
	}

}
