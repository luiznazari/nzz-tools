package br.com.senior.test.http;

import br.com.senior.test.utils.EncodeUtils;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * Builder for building parameterized and valid URIs.
 * <br><br>
 * Basic usage:
 * <pre>
 * String uri = new UriBuilder("/base/uri")
 *     .param("term", "value")
 *     .param("special", "value w!th $p&ci@l ch@r@ct&r&$")
 *     .build();
 *
 * // uri = /base/uri?term=value&special=value+w%21th+%24p%26ci%40l+ch%40r%40ct%26r%26%24
 * </pre>
 *
 * @author Luiz.Nazari
 */
public class UriBuilder {

	private final Map<String, Object> params;
	private final String uri;

	public UriBuilder(String uri) {
		this.uri = uri;
		this.params = Maps.newHashMap();
	}

	public UriBuilder param(String name, Object value) {
		this.params.put(name, value);
		return this;
	}

	public String build() {
		StringBuilder sb = new StringBuilder(this.uri);

		if (!this.params.isEmpty()) {
			Iterator<Map.Entry<String, Object>> entryIterator = this.params.entrySet().iterator();

			sb.append('?');
			appendUriParameter(sb, entryIterator.next());
			while (entryIterator.hasNext()) {
				sb.append('&');
				appendUriParameter(sb, entryIterator.next());
			}
		}

		return sb.toString();
	}

	private void appendUriParameter(StringBuilder sb, Map.Entry<String, Object> param) {
		sb.append(EncodeUtils.encodeUrl(param.getKey()));
		sb.append('=');
		sb.append(EncodeUtils.encodeUrl(String.valueOf(param.getValue())));
	}

}
