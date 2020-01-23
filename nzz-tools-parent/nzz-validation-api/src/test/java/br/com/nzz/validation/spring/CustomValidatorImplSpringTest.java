package br.com.nzz.validation.spring;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.nzz.validation.AbstractCustomValidatorImplTest;
import br.com.nzz.validation.CustomValidator;
import lombok.Getter;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = SpringTestConfiguration.class)
public class CustomValidatorImplSpringTest extends AbstractCustomValidatorImplTest {

	@Getter
	@Autowired
	private CustomValidator validator;

	@Before
	public void setUp() {
		assertNotNull("Should successfully auto wire the validator with Spring container.", validator);
	}


}
