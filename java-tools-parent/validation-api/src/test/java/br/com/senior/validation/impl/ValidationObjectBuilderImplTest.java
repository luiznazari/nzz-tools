package br.com.senior.validation.impl;

import br.com.senior.validation.BeautifulObject;
import br.com.senior.validation.TestValidationRule;
import br.com.senior.validation.ValidationObjectBuilder;
import br.com.senior.validation.ValidationResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ValidationObjectBuilderImplTest extends ValidationBuilderImplTest {

	@Override
	protected ValidationObjectBuilder<BeautifulObject> getValidationBuilder() {
		return (ValidationObjectBuilder<BeautifulObject>) super.validationBuilder;
	}

	@Before
	public void setUp() {
		BeautifulObject object = new BeautifulObject();
		super.validationBuilder = new ValidationObjectBuilderImpl<>(object);
	}

	@Test
	public void shouldBuildAndValidate() {
		ValidationResult result = getValidationBuilder()
			.with(new TestValidationRule())
			.validate();

		assertNotNull(result);
		assertTrue(result.hasErrors());
	}

}
