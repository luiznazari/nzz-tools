package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.Environment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Defines a SOAP WebServices with two possible URLs, one for production,
 * one for development.
 *
 * @author Luiz Felipe Nazari
 */
@Getter
@AllArgsConstructor
public class SoapWebService {

	private static final String WSDL_PARAM = "?WSDL";

	private final String urlProduction;

	private final String urlDevelopment;

	public String getUrl(Environment environment) {
		return environment == Environment.PRODUCTION ? this.urlProduction : this.urlDevelopment;
	}

	public String getUrlWsdl(Environment environment) {
		String soapUrl = this.getUrl(environment);
		if (!StringUtils.endsWithIgnoreCase(soapUrl, WSDL_PARAM)) {
			return this.getUrl(environment) + WSDL_PARAM;
		}
		return soapUrl;
	}

	public static SoapWebService from(String url) {
		return new SoapWebService(url, url);
	}


}
