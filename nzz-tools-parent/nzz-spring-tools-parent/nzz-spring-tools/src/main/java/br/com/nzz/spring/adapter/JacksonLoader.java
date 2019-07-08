package br.com.nzz.spring.adapter;

import com.fasterxml.jackson.databind.JsonSerializer;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JacksonLoader extends NzzLoader {

	public static List<JsonSerializer<?>> loadSerializersFromClassPath() {
		List<JsonSerializer<?>> serializers = new ArrayList<>();
		if (isNzzCommonsEnabled()) {
			serializers.add(instantiateClass("br.com.nzz.spring.adapter.ValorDecimalJsonSerializer"));
		}
		if (isNzzSpringWsEnabled()) {
			serializers.add(instantiateClass("br.com.nzz.spring.ws.adapter.XMLGregorianCalendarJsonSerializer"));
		}
		return serializers;
	}

}
