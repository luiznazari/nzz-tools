package br.com.nzz.spring.ws.adapter;

import lombok.experimental.UtilityClass;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class AdapterLoader {

	public static XmlAdapter<?, ?>[] loadFromClassPath() {
		List<XmlAdapter<?, ?>> adapters = new ArrayList<>();
		if (AdapterLoader.isNzzCommonsEnabled()) {
			adapters.add(instantiateClass("br.com.nzz.spring.ws.adapter.LocalDateBrXmlAdapter"));
			adapters.add(instantiateClass("br.com.nzz.spring.ws.adapter.LocalDateTimeBrXmlAdapter"));
			adapters.add(instantiateClass("br.com.nzz.spring.ws.adapter.ValorDecimalXmlAdapter"));
		}
		return adapters.toArray(new XmlAdapter<?, ?>[0]);
	}

	private static boolean isNzzCommonsEnabled() {
		try {
			AdapterLoader.class.getClassLoader().loadClass("br.com.nzz.commons.NzzDateUtils");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T instantiateClass(String className) {
		try {
			return (T) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
