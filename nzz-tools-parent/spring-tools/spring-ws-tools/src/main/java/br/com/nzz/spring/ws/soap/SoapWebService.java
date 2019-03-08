package br.com.nzz.spring.ws.soap;

import br.com.nzz.spring.ws.Environment;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Especificação dos WebServices SOAP consumidos pela aplicação.
 *
 * @author Luiz Felipe Nazari
 */
@Getter
@AllArgsConstructor
public class SoapWebService {

	private final String urlProduction;

	private final String urlDevelopment;

	public String getUrl(Environment environment) {
		return environment == Environment.PRODUCTION ? this.urlProduction : this.urlDevelopment;
	}

	public String getUrlWsdl(Environment environment) {
		return this.getUrl(environment) + "?WSDL";
	}

}
