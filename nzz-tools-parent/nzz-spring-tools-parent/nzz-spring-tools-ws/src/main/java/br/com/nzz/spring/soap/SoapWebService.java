package br.com.nzz.spring.soap;

import org.apache.commons.lang3.StringUtils;

import br.com.nzz.spring.Environment;
import br.com.nzz.spring.WebService;
import lombok.Getter;

/**
 * Defines a SOAP WebServices with three possible URLs, one for production,
 * one for staging and one for development.
 *
 * @author Luiz Felipe Nazari
 */
@Getter
public class SoapWebService extends WebService {

	private static final String WSDL_PARAM = "?WSDL";

	public SoapWebService(String urlProduction, String urlStaging, String urlDevelopment) {
		super(normalizeUrl(urlProduction), normalizeUrl(urlStaging), normalizeUrl(urlDevelopment));
	}

	private static String normalizeUrl(String url) {
		return StringUtils.endsWithIgnoreCase(url, WSDL_PARAM) ? url.substring(0, WSDL_PARAM.length()) : url;
	}

	public static SoapWebService from(String url) {
		return new SoapWebService(url, url, url);
	}

	public String getUrlWsdl(Environment environment) {
		String soapUrl = this.getUrl(environment);
		if (!StringUtils.endsWithIgnoreCase(soapUrl, WSDL_PARAM)) {
			return this.getUrl(environment) + WSDL_PARAM;
		}
		return soapUrl;
	}

}
