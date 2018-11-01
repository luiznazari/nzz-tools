package br.com.senior.validation.impl;

import br.com.senior.validation.BeautifulObject;
import br.com.senior.validation.SimpleTestError;
import br.com.senior.volkswagen.test.UnitTest;
import  br.com.senior.validation.ValidationRule;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class ObjectValidatorImplTest extends UnitTest {

	private ObjectValidatorImpl<BeautifulObject> objectValidator;

	@Before
	public void setUp() {
		this.objectValidator = new ObjectValidatorImpl<>();
	}

	@Test
	public void shouldSeparateValidationBlocks() {
		ValidationBlock<BeautifulObject> validationBlock1 = objectValidator.currentValidationBlock();
		assertNotNull(validationBlock1);
		assertEquals(validationBlock1, objectValidator.currentValidationBlock());

		objectValidator.finalizeCurrentValidationBlock();

		ValidationBlock<BeautifulObject> validationBlock2 = objectValidator.currentValidationBlock();
		assertNotNull(validationBlock2);
		assertEquals(validationBlock2, objectValidator.currentValidationBlock());
		assertNotEquals(validationBlock1, validationBlock2);
	}

	@Test
	public void shouldValidateObject() {
		BeautifulObject object = mock(BeautifulObject.class);

		ValidationRule<BeautifulObject> validationRuleBlock1 = mock(ValidationRule.class);
		ValidationRule<BeautifulObject> validationRuleBlock2 = mock(ValidationRule.class);

		objectValidator.currentValidationBlock().add(validationRuleBlock1);
		objectValidator.finalizeCurrentValidationBlock();
		objectValidator.currentValidationBlock().add(validationRuleBlock2);

		objectValidator.validate(object);
		verify(validationRuleBlock1).validate(object);
		verify(validationRuleBlock2).validate(object);
	}

	@Test
	public void shouldNotValidateBlock1WhenBlock1ReturnsErrors() {
		BeautifulObject object = mock(BeautifulObject.class);

		ValidationRule<BeautifulObject> validationRuleBlock1 = mock(ValidationRule.class);
		ValidationRule<BeautifulObject> validationRuleBlock2 = mock(ValidationRule.class);

		objectValidator.currentValidationBlock().add(validationRuleBlock1);
		objectValidator.finalizeCurrentValidationBlock();
		objectValidator.currentValidationBlock().add(validationRuleBlock2);

		when(validationRuleBlock1.validate(object)).thenReturn(Sets.newHashSet(new SimpleTestError()));

		objectValidator.validate(object);
		verify(validationRuleBlock1, once()).validate(object);
		verify(validationRuleBlock2, never()).validate(object);
	}

}
