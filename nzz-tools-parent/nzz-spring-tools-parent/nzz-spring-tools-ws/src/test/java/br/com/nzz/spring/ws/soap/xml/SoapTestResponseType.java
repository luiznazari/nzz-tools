package br.com.nzz.spring.ws.soap.xml;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.nzz.spring.adapter.LocalDateBrXmlAdapter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soapTestResponse")
@XmlRootElement(name = "soapTestResponse")
public class SoapTestResponseType {

	private String name;

	private String nickname;

	@XmlJavaTypeAdapter(LocalDateBrXmlAdapter.class)
	private LocalDate birthDate;

}
