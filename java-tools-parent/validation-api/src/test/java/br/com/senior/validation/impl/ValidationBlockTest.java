package br.com.senior.validation.impl;

import br.com.senior.validation.SimpleTestError;
import br.com.senior.validation.ValidationError;
import br.com.senior.validation.ValidationRule;
import br.com.senior.validation.exception.CustomValidationException;
import br.com.senior.test.UnitTest;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;

@SuppressWarnings("unchecked")
public class ValidationBlockTest extends UnitTest {

	@Test
	public void shouldCreateValidationBlockWithRulesInOrder() {
		ValidationRule<Object> validationRule1 = mock(ValidationRule.class);
		ValidationRule<Object> validationRule2 = mock(ValidationRule.class);
		Object object = mock(Object.class);

		ValidationBlock<Object> block = new ValidationBlock<>();
		block.add(validationRule1);
		block.add(validationRule2);

		block.execute(object);

		InOrder inOrder = inOrder(validationRule1, validationRule2);
		inOrder.verify(validationRule1).validate(object);
		inOrder.verify(validationRule2).validate(object);
	}

	@Test
	public void shouldCreateValidationBlockWithRulesAndSupplierInOrder() {
		ValidationRule<Object> validationRule1 = mock(ValidationRule.class);
		ValidationRule<Object> validationRule2 = mock(ValidationRule.class);
		Supplier<ValidationError> validationErrorSupplier = mock(Supplier.class);
		when(validationErrorSupplier.get()).thenReturn(mock(ValidationError.class));

		Object object = mock(Object.class);

		ValidationBlock<Object> block = new ValidationBlock<>();
		block.add(validationRule2);
		block.add(validationErrorSupplier);
		block.add(validationRule1);

		block.execute(object);

		InOrder inOrder = inOrder(validationRule2, validationErrorSupplier, validationRule1);
		inOrder.verify(validationRule2).validate(object);
		inOrder.verify(validationErrorSupplier).get();
		inOrder.verify(validationRule1).validate(object);
	}

	@Test
	public void shouldExecuteAndReturnErrors() {
		Object object = mock(Object.class);

		ValidationRule<Object> validationRule = mock(ValidationRule.class);
		when(validationRule.validate(object)).thenReturn(Sets.newHashSet(new SimpleTestError(), new SimpleTestError()));

		ValidationRule<Object> validationRuleThrowingException = mock(ValidationRule.class);
		when(validationRuleThrowingException.validate(object)).thenThrow(new CustomValidationException("test.error"));

		Supplier<ValidationError> validationErrorSupplier = mock(Supplier.class);
		when(validationErrorSupplier.get()).thenReturn(new SimpleTestError());

		ValidationBlock<Object> block = new ValidationBlock<>();
		block.add(validationRule);
		block.add(validationErrorSupplier);
		block.add(validationRuleThrowingException);

		Set<ValidationError> validationErrors = block.execute(object);
		assertNotNull(validationErrors);
		assertUnmodifiableCollection(validationErrors);
		assertEquals(4, validationErrors.size());
	}

	private void assertUnmodifiableCollection(Collection<ValidationError> validationErrors) {
		if (!validationErrors.getClass().getName().contains("Unmodifiable")) {
			fail("Collections must be unmodifiable!");
		}
	}

}
