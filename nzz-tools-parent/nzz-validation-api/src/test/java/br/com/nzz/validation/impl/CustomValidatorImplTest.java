package br.com.nzz.validation.impl;

import br.com.nzz.validation.*;
import br.com.nzz.validation.exception.CustomValidationException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class CustomValidatorImplTest {

	private BeautifulObject theMostBeautifulObject;
	private final CustomValidator validator;

	public CustomValidatorImplTest() {
		CustomValidatorImpl customValidator = new CustomValidatorImpl();
		customValidator.setValidator(new BeanValidator());
		this.validator = customValidator;
	}

	@Before
	public void setUp() {
		theMostBeautifulObject = new BeautifulObject();
		theMostBeautifulObject.setName("Praise the Sun!");
		theMostBeautifulObject.setWhatImDoing("Praising the Sun!");
		theMostBeautifulObject.setId(1L);

		verifyIfValidObjectIsReallyValid();
	}

	private void verifyIfValidObjectIsReallyValid() {
		validator
			.validate(validObject())
			.onError(errors -> fail("This test suite assumes that validObject() returns a valid object!"));
	}

	@Test
	public void shouldExecuteBeanValidationOnObject() {
		BeautifulObject object = new BeautifulObject();

		ValidationResult validationResult = validator.validate(object);

		assertTrue(validationResult.hasErrors());
		Set<ValidationError> validationErrors = validationResult.getErrors();
		assertTrue(validationErrors.stream().anyMatch(msgCode(BeautifulObject.ERROR_MSG_NAME_EMPTY)));
		assertTrue(validationErrors.stream().anyMatch(msgCode(BeautifulObject.ERROR_MSG_WHAT_IM_DOING_EMPTY)));
		assertTrue(validationErrors.stream().anyMatch(msgCode(BeautifulObject.ERROR_MSG_WMBCF_EMPTY)));
	}

	@Test
	public void shouldExecuteBeanValidationOnObjectWithValues() {
		BeautifulObject object = new BeautifulObject();
		object.setName(RandomStringUtils.randomAlphabetic(10));
		object.setWhatImDoing(RandomStringUtils.randomAlphabetic(101));
		object.setWhereMyBeautyCameFrom(theMostBeautifulObject);

		ValidationResult validationResult = validator.validate(object);

		assertTrue(validationResult.hasErrors());
		Set<ValidationError> validationErrors = validationResult.getErrors();
		assertTrue(validationErrors.stream().anyMatch(msgCode(BeautifulObject.ERROR_MSG_WHAT_IM_DOING_INVALID_RANGE)));
	}

	@Test
	public void shouldReturnEmptyErrorsWhenNoErrors() {
		BeautifulObject validObject = validObject();

		ValidationResult validationResult = validator.validate(validObject);
		assertFalse(validationResult.hasErrors());
	}

	@Test
	public void shouldAddBeanValidationToCustomValidation() {
		BeautifulObject object = validObject();
		object.setName("");

		ValidationResult validationResult = validator
			.validate(object)
			.validate(() -> {
				throw new CustomValidationException(SimpleTestError.DEFAULT_ERROR_MSG_CODE);
			});

		assertTrue(validationResult.hasErrors());
		Set<ValidationError> validationErrors = validationResult.getErrors();
		assertTrue(validationErrors.stream().anyMatch(msgCode(BeautifulObject.ERROR_MSG_NAME_EMPTY)));
		assertTrue(validationErrors.stream().anyMatch(msgCode(SimpleTestError.DEFAULT_ERROR_MSG_CODE)));
	}

	private BeautifulObject validObject() {
		BeautifulObject object = new BeautifulObject();
		object.setName(RandomStringUtils.randomAlphabetic(10));
		object.setWhatImDoing(RandomStringUtils.randomAlphabetic(100));
		object.setWhereMyBeautyCameFrom(theMostBeautifulObject);
		return object;
	}

	private Predicate<? super ValidationError> msgCode(String errorMessageCode) {
		return error -> error.getMessageKey().equals(errorMessageCode);
	}

}
