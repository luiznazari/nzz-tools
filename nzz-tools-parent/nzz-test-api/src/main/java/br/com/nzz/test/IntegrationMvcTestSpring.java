package br.com.nzz.test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class IntegrationMvcTestSpring extends IntegrationTestSpring {

	@Autowired
	protected WebApplicationContext context;

	protected MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

}
