package br.com.nzz.spring.ws.soap;

import org.junit.Test;

import br.com.nzz.spring.ws.Environment;
import br.com.nzz.spring.ws.WebService;

import static org.junit.Assert.assertEquals;

public class SoapWebServiceTest {

	@Test
	public void shouldCreateWithWsdlUrls() {
		final String url = "http://localhost:42428/fake-soap/sapiens_Synccom_senior_g5_co_mcm_cpr_notafiscal?wsdl";
		SoapWebService soapWebService = SoapWebService.from(url);

		final String expectedUrl = "http://localhost:42428/fake-soap/sapiens_Synccom_senior_g5_co_mcm_cpr_notafiscal";
		assertEquals(expectedUrl, soapWebService.getUrlDevelopment());
		assertEquals(expectedUrl, soapWebService.getUrlStaging());
		assertEquals(expectedUrl, soapWebService.getUrlProduction());
	}

	@Test
	public void shouldCreateFromWebServiceTemplate() {
		WebService ws = new WebService(
			"http://fake-url/prod",
			"http://fake-url/staging",
			"http://fake-url/dev");
		SoapWebService soapWebService = SoapWebService.from(ws);

		assertEquals(ws.getUrlDevelopment(), soapWebService.getUrlDevelopment());
		assertEquals(ws.getUrlStaging(), soapWebService.getUrlStaging());
		assertEquals(ws.getUrlProduction(), soapWebService.getUrlProduction());
		assertEquals(ws.getUrlDevelopment() + "?WSDL", soapWebService.getUrlWsdl(Environment.DEVELOPMENT));
	}

}