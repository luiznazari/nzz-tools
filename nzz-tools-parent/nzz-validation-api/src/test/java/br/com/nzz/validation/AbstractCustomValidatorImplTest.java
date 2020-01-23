package br.com.nzz.validation;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import br.com.nzz.validation.impl.rules.NotNullValidationRule;
import lombok.RequiredArgsConstructor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RequiredArgsConstructor
public abstract class AbstractCustomValidatorImplTest {

	private final String STRING_NOT_EMPTY_ERROR_CODE = "test.error.not.empty";
	private final Supplier<ValidationError> notEmptyValidationConstraint = ValidationConstraints.notEmpty("", STRING_NOT_EMPTY_ERROR_CODE);

	protected abstract CustomValidator getValidator();

	@Test
	public void shouldValidateWithCustomValidationClass() {
		ObjectValidator<BeautifulObject> beautifulObjectValidator = buildValidator();
		assertNotNull("The created object validator should not be null!", beautifulObjectValidator);

		try {
			ValidationResult validationResult = beautifulObjectValidator.validate(new BeautifulObject());
			assertNotNull("Validator should never return a null result.", validationResult);
		} catch (Throwable t) { // NOSONAR
			fail("Should successfully validate an object with the built validator.");
		}
	}

	@Test
	public void shouldBlockValidationAfterABlockingValidationError() {
		ValidationResult result = buildValidator().validate(null);
		assertErrors(result, NotNullValidationRule.ERROR);
	}

	private ObjectValidator<BeautifulObject> buildValidator() {
		return getValidator().builder(BeautifulObject.class)
			.with(new NotNullValidationRule()).blocking()
			.withBeanValidation().blocking()
			.with(TestValidationRule.class)
			.with(TestIdNotNullValidator.class)
			.with(notEmptyValidationConstraint)
			.build();
	}

	private void assertErrors(ValidationResult validationResult, String... expectedErrorCodes) {
		List<String> errorCodes = validationResult.getErrors().stream().map(ValidationError::getMessageKey).collect(Collectors.toList());

		boolean sameErrorCount = errorCodes.size() == expectedErrorCodes.length;
		boolean containsAllExpectedErrors = errorCodes.containsAll(Arrays.asList(expectedErrorCodes));

		if (!sameErrorCount || !containsAllExpectedErrors) {
			StringBuilder sb = new StringBuilder("Ops! The br.com.nzz.validation returned unexpected errors!\n");
			sb.append("\tExpected errors:\n");
			Arrays.stream(expectedErrorCodes).forEach(errorCode -> sb.append("\t\t").append(errorCode).append('\n'));
			sb.append("\tObtained errors:\n");
			errorCodes.forEach(errorCode -> sb.append("\t\t").append(errorCode).append('\n'));
			fail(sb.toString());
		}
	}

}
