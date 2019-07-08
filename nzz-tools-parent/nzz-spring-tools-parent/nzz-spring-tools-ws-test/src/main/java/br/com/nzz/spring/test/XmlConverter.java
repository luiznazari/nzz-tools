package br.com.nzz.spring.test;

import org.dom4j.io.XMLResult;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import br.com.nzz.spring.exception.WebServiceInternalException;

/**
 * Utility converter class for XML mapped classes.
 *
 * @author Luiz Felipe Nazari
 */
class XmlConverter {

	private final Jaxb2Marshaller marshaller;

	XmlConverter(Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	String toXml(Object obj) throws WebServiceInternalException {
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
			XMLResult result = new XMLResult(outStream);
			marshaller.marshal(obj, result);
			return outStream.toString();

		} catch (XmlMappingException | IOException e) {
			throw new WebServiceInternalException("Could not convert object to XML: " + e.getMessage(), e);
		}
	}

	<T> JAXBElement<T> toObject(String xml, Class<T> clazz) throws WebServiceInternalException {
		return toObject(new StringSource(xml), clazz);
	}

	<T> JAXBElement<T> toObject(InputStream xmlInputStream, Class<T> clazz) throws WebServiceInternalException {
		return toObject(new StreamSource(xmlInputStream), clazz);
	}

	@SuppressWarnings("unchecked")
	private <T> JAXBElement<T> toObject(Source source, Class<T> clazz) throws WebServiceInternalException {
		try {
			this.marshaller.setMappedClass(clazz);
			Object element = this.marshaller.unmarshal(source);
			if (element instanceof JAXBElement) {
				return (JAXBElement<T>) element;
			}
			return new JAXBElement<>(new QName(clazz.getSimpleName()), clazz, (T) element);
		} catch (XmlMappingException e) {
			throw new WebServiceInternalException("Could not convert stream to object:\n" + e.getMessage(), e);
		}
	}

}
