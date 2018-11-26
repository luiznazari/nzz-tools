package br.com.senior.validation.impl;

import br.com.senior.validation.*;
import br.com.senior.validation.impl.rules.NotNullValidationRule;
import br.com.senior.test.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ValidationBuilderImplTest extends UnitTest {

	ValidationBuilder<BeautifulObject> validationBuilder;

	protected ValidationBuilder<BeautifulObject> getValidationBuilder() {
		return validationBuilder;
	}

	@Before
	public void setUp() {
		this.validationBuilder = new ValidationBuilderImpl<>();
	}

	@Test
	public void shouldBuildEmptyValidatorSuccessfully() {
		validationBuilder.build();
	}

	@Test
	public void shouldBuildValidatorSuccessfully() {
		Supplier<ValidationError> errorSupplier = SimpleTestError::new;

		ObjectValidator<BeautifulObject> objectValidator = validationBuilder
			.with(TestValidationRule.class)
			.with(new TestIdNotNullValidator())
			.with(errorSupplier)
			.build();

		assertNotNull(objectValidator);
		assertNotNull(objectValidator.validate(new BeautifulObject()));
	}

	@Test(expected = NullPointerException.class)
	public void shouldExecuteValidatorsInOrder() {
		validationBuilder
			.with(NotNullValidationRule.class)
			.with(TestIdNotNullValidator.class) // This br.com.senior.validation requires not null object.
			.build()
			.validate(null);
	}

	@Test
	public void shouldBlockExecuteValidatorsInOrder() {
		ValidationResult result = validationBuilder
			.with(NotNullValidationRule.class).blocking()
			.with(TestIdNotNullValidator.class) // This br.com.senior.validation requires not null object.
			.build()
			.validate(null);
		assertTrue(result.hasErrors());
	}

}
