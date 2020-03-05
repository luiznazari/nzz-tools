package br.com.nzz.spring.ws.utils;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.mockito.stubbing.Answer;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.test.client.MockWebServiceServer;

import br.com.nzz.spring.ws.soap.GenericSoapRequest;

public interface MockSoapRequests {

	static Answer<GenericSoapRequest> configureMockWebServiceServer(UnSafeConsumer<MockWebServiceServer> mockWebServiceServerConsumer) {
		return configureMockWebServiceServer(mockWebServiceServerConsumer, null);
	}

	static Answer<GenericSoapRequest> configureMockWebServiceServer(UnSafeConsumer<MockWebServiceServer> mockWebServiceServerConsumer, MutableObject<MockWebServiceServer> mockWebServiceServerHolder) {
		return answer -> {
			GenericSoapRequest genericSoapRequest = (GenericSoapRequest) answer.callRealMethod();
			genericSoapRequest.doWithWebServiceGateway(wsGateway -> {
				try {
					MockWebServiceServer mockWebServiceServer = MockWebServiceServer.createServer((WebServiceGatewaySupport) wsGateway);
					mockWebServiceServerConsumer.accept(mockWebServiceServer);
					if (mockWebServiceServerHolder != null) {
						mockWebServiceServerHolder.setValue(mockWebServiceServer);
					}
				} catch (Exception e) {
					LogManager.getLogger(MockSoapRequests.class).error(e);
					Assert.fail(e.getMessage());
				}
			});

			return genericSoapRequest;
		};
	}

	static void verifyWebServiceServer(MutableObject<MockWebServiceServer> mockWebServiceServerHolder) {
		if (mockWebServiceServerHolder.getValue() != null) {
			mockWebServiceServerHolder.getValue().verify();
		} else {
			Assert.fail("Wanted but not invoked WebService Server request. The MockWebServiceServer is null.");
		}
	}

}
