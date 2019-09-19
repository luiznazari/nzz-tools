package br.com.nzz.spring.ws.soap;

import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.mime.MimeContainer;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import lombok.Setter;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockJaxb2Marshaller extends Jaxb2Marshaller {

	@Setter
	private Object response;
	private final JAXBContext jaxbContext;

	MockJaxb2Marshaller() {
		this.jaxbContext = mock(JAXBContext.class);
		try {
			when(this.jaxbContext.createMarshaller()).thenReturn(mock(Marshaller.class));
			when(this.jaxbContext.createUnmarshaller()).thenReturn(mock(Unmarshaller.class));
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Override
	public void marshal(@Nonnull Object graph, @Nonnull Result result) throws XmlMappingException {
		super.marshal(graph, result);
	}

	@Nonnull
	@Override
	public Object unmarshal(@Nonnull Source source, MimeContainer mimeContainer) throws XmlMappingException {
		return this.response;
	}

	@Nonnull
	@Override
	public JAXBContext getJaxbContext() {
		return this.jaxbContext;
	}

}