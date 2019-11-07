package br.com.nzz.spring.ws.utils;

import org.junit.Assert;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.InputStream;
import java.util.function.Consumer;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import br.com.nzz.spring.exception.WebServiceInternalException;
import br.com.nzz.spring.ws.soap.TypedSoapRequest;
import br.com.nzz.test.UnitTest;
import br.com.nzz.validation.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe utilitária to testes com XML.
 *
 * @author Luiz Felipe Nazari
 */
@SuppressWarnings("unused")
@Slf4j
public class XmlTestUtils {

	private XmlTestUtils() {
	}

	public static String objToXml(Object obj) {
		return objToXml(TypedSoapRequest.createMarshaller(obj.getClass()), obj);
	}

	public static String objToXml(Jaxb2Marshaller marshaller, Object obj) {
		try {
			return new XmlConverter(marshaller).toXml(obj);
		} catch (WebServiceInternalException e) {
			UnitTest.fail(e);
			return null;
		}
	}

	public static <T> T xmlToObj(String xml, Class<T> classe) {
		return xmlToObj(TypedSoapRequest.createMarshaller(classe), xml, classe);
	}

	public static <T> T xmlToObj(Jaxb2Marshaller unMarshaller, String xml, Class<T> classe) {
		try {
			return new XmlConverter(unMarshaller).toObject(xml, classe).getValue();
		} catch (WebServiceInternalException e) {
			UnitTest.fail(e);
			return null;
		}
	}

	public static <T> T xmlToObj(InputStream xmlInputStream, Class<T> objectClass) {
		return xmlToObj(TypedSoapRequest.createMarshaller(objectClass), xmlInputStream, objectClass);
	}

	public static <T> T xmlToObj(Jaxb2Marshaller unMarshaller, InputStream xmlInputStream, Class<T> objectClass) {
		try {
			return new XmlConverter(unMarshaller).toObject(xmlInputStream, objectClass).getValue();
		} catch (WebServiceInternalException e) {
			UnitTest.fail(e);
			return null;
		}
	}

	public static XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(lexicalRepresentation);
		} catch (DatatypeConfigurationException e) {
			UnitTest.fail(e);
		}
		return null;
	}

	public static void mustThrowException(Runnable runnable) {
		XmlTestUtils.mustThrowException("Esperava um AssertionError e foi obtido sucesso.", runnable, null, null);
	}

	public static void mustThrowException(String msgFail, Runnable runnable) {
		XmlTestUtils.mustThrowException(msgFail, runnable, Throwable.class, null);
	}

	public static <T extends Throwable> void mustThrowException(Runnable runnable, Class<T> classeErro, Consumer<T> tratarErro) {
		XmlTestUtils.mustThrowException("Esperava um " + classeErro.getSimpleName() + " e foi obtido sucesso.", runnable, classeErro,
			tratarErro);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> void mustThrowException(String msgFail, Runnable runnable, Class<T> classeErro,
	                                                            Consumer<T> tratarErro) {
		try {
			runnable.run();
			Assert.fail(msgFail);
		} catch (Throwable t) {
			if (tratarErro != null && t.getClass().equals(classeErro)) {
				tratarErro.accept((T) t);
			} else {
				throw t;
			}
		}
	}

	/**
	 * Compara um {@link Error} com um {@link ErrorMessage} avaliando os códigos de
	 * erro.
	 *
	 * @param erroEsperado erro esperado
	 * @param erroObtido   erro obtido
	 */
	public static void assertError(ErrorMessage erroEsperado, ErrorMessage erroObtido) {
		Assert.assertEquals(erroEsperado.getMessageKey(), erroObtido.getMessageKey());
	}

}
