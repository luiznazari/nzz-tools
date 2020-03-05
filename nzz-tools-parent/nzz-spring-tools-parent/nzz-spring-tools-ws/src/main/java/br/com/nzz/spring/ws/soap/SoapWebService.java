package br.com.nzz.spring.ws.soap;

import org.apache.commons.lang3.StringUtils;

import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.WebService;
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
		if (StringUtils.endsWithIgnoreCase(url, WSDL_PARAM)) {
			int wsdlIndex = StringUtils.indexOfIgnoreCase(url, WSDL_PARAM);
			return url.substring(0, wsdlIndex);
		}
		return url;
	}

	public static SoapWebService from(String url) {
		return new SoapWebService(url, url, url);
	}

	public static SoapWebService from(WebService webService) {
		return new SoapWebService(webService.getUrlProduction(), webService.getUrlStaging(), webService.getUrlDevelopment());
	}

	public String getUrlWsdl(Environment environment) {
		String soapUrl = this.getUrl(environment);
		if (!StringUtils.endsWithIgnoreCase(soapUrl, WSDL_PARAM)) {
			return this.getUrl(environment) + WSDL_PARAM;
		}
		return soapUrl;
	}

}
