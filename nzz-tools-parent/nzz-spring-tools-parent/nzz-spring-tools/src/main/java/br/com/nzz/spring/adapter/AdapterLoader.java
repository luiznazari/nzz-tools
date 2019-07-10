package br.com.nzz.spring.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AdapterLoader extends NzzLoader {

	public static XmlAdapter<?, ?>[] loadFromClassPath() {
		List<XmlAdapter<?, ?>> adapters = new ArrayList<>();
		if (AdapterLoader.isNzzCommonsEnabled()) {
			adapters.add(instantiateClass("LocalDateBrXmlAdapter"));
			adapters.add(instantiateClass("LocalDateTimeBrXmlAdapter"));
			adapters.add(instantiateClass("ValorDecimalXmlAdapter"));
		}
		return adapters.toArray(new XmlAdapter<?, ?>[0]);
	}

}
