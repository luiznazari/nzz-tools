package br.com.nzz.spring.ws.adapter;

abstract class NzzLoader {

	static boolean isNzzCommonsEnabled() {
		try {
			AdapterLoader.class.getClassLoader().loadClass("br.com.nzz.commons.NzzDateUtils");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
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
