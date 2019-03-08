package br.com.senior.test.utils;

import br.com.senior.test.exception.VolkswagenTestException;
import com.google.common.base.Charsets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class EncodeUtils {

	private EncodeUtils() {}

	public static String encodeUrl(String string) {
		try {
			return URLEncoder.encode(string, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new VolkswagenTestException(e);
		}
	}

}
