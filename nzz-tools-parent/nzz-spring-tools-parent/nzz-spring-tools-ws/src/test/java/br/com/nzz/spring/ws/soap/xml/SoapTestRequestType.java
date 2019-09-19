package br.com.nzz.spring.ws.soap.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soapTestRequest")
@XmlRootElement(name = "soapTestRequest")
public class SoapTestRequestType {

	private String username;

	private String password;

}
