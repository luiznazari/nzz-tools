package br.com.nzz.spring.adapter;

abstract class NzzLoader {

	private static final boolean nzzCommonsEnabled;
	private static final boolean nzzSpringWsEnabled;

	static {
		nzzCommonsEnabled = isClassInClassPath("br.com.nzz.commons.NzzDateUtils");
		nzzSpringWsEnabled = isClassInClassPath("br.com.nzz.spring.ws.soap.SoapClient");
	}

	private static boolean isClassInClassPath(String className) {
		try {
			AdapterLoader.class.getClassLoader().loadClass(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	static boolean isNzzCommonsEnabled() {
		return nzzCommonsEnabled;
	}

	static boolean isNzzSpringWsEnabled() {
		return nzzSpringWsEnabled;
	}

	@SuppressWarnings("unchecked")
	static <T> T instantiateClass(String className) {
		try {
			return (T) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
