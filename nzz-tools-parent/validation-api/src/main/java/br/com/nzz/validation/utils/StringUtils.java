package br.com.nzz.validation.utils;

public interface StringUtils {

	static boolean isEmpty(String group) {
		return group == null || group.isEmpty();
	}

	static boolean isNotEmpty(String group) {
		return group != null && !group.isEmpty();
	}

	static String toString(Object[] array) {
		if (array == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(']');
		return sb.toString();
	}

}
