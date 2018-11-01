package br.com.senior.validation.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspare.core.Component;
import br.com.senior.validation.*;

import javax.inject.Inject;
import javax.validation.Validator;
import java.util.function.Supplier;

@Component
@NoArgsConstructor
public class CustomValidatorImpl implements CustomValidator {

	@Inject
	@Setter(AccessLevel.PACKAGE)
	private Validator validator;

	@Override
	public ValidationResult validate(Object object) {
		return ValidationResult.of(this.validator.validate(object));
	}

	@Override
	public ValidationResult validate(Supplier<ValidationError> errorSupplier) {
		return ValidationResult.empty().validate(errorSupplier);
	}

	@Override
	public <T> ValidationBuilder<T> builder(Class<T> targetObjectClass) {
		return new ValidationBuilderImpl<>();
	}

	@Override
	public <T> ValidationObjectBuilder<T> builder(T target) {
		return new ValidationObjectBuilderImpl<>(target);
	}

}
