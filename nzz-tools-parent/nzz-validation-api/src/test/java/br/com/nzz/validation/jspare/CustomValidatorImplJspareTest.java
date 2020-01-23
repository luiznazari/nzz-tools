package br.com.nzz.validation.jspare;

import org.jspare.core.Environment;
import org.jspare.unit.ext.junit.JspareUnitRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.validation.Validator;

import br.com.nzz.validation.AbstractCustomValidatorImplTest;
import br.com.nzz.validation.CustomValidator;
import br.com.nzz.validation.impl.BeanValidator;
import br.com.nzz.validation.impl.CustomValidatorImpl;
import lombok.Getter;

import static org.jspare.core.internal.Bind.bind;
import static org.junit.Assert.assertNotNull;

@RunWith(JspareUnitRunner.class)
public class CustomValidatorImplJspareTest extends AbstractCustomValidatorImplTest {

	@Getter
	@Inject
	private CustomValidator validator;

	@BeforeClass
	public static void setUpJspare() {
		Environment.registry(bind(Validator.class).to(BeanValidator.class));
		Environment.registry(bind(CustomValidator.class).to(CustomValidatorImpl.class));
	}

	@Before
	public void setUp() {
		assertNotNull("Should successfully inject the validator with Jspare container.", validator);
	}

}
