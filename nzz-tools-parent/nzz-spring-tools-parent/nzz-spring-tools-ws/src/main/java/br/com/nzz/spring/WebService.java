package br.com.nzz.spring;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines a WebServices with three possible URLs, one for production,
 * one for staging and one for development.
 *
 * @author Luiz Felipe Nazari
 */
@Getter
@AllArgsConstructor
public class WebService {

	private final String urlProduction;

	private final String urlStaging;

	private final String urlDevelopment;

	public static WebService from(String url) {
		return new WebService(url, url, url);
	}

	public String getUrl(Environment environment) {
		if (Environment.PRODUCTION == environment) {
			return this.urlDevelopment;
		} else if (Environment.STAGING == environment) {
			return this.urlStaging;
		}
		return this.urlDevelopment;
	}

}
