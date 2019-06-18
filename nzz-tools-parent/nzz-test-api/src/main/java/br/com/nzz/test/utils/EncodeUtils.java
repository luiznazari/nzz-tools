package br.com.nzz.test.utils;

import br.com.nzz.test.exception.NzzTestException;
import com.google.common.base.Charsets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class EncodeUtils {

	private EncodeUtils() {}

	public static String encodeUrl(String string) {
		try {
			return URLEncoder.encode(string, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new NzzTestException(e);
		}
	}

}
