package br.com.nzz.spring.ws.adapter;

import br.com.nzz.commons.NzzNumberUtils;
import br.com.nzz.commons.ValorDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Optional;

public class ValorDecimalXmlAdapter extends XmlAdapter<String, ValorDecimal> {
	@Override
	public ValorDecimal unmarshal(String value) {
		Optional<Double> doubleValue = NzzNumberUtils.extractNumber(value);
		if (doubleValue.isPresent()) {
			return ValorDecimal.valorPara(doubleValue.get());
		} else {
			return null;
		}
	}

	@Override
	public String marshal(ValorDecimal valorDecimal) {
		return valorDecimal.getValorFormatado();
	}
}
