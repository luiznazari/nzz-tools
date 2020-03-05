package br.com.nzz.validation.impl;

import br.com.nzz.validation.*;
import lombok.AccessLevel;
import lombok.Setter;
import org.jspare.core.Component;

import javax.validation.Validator;
import java.util.function.Supplier;

@Component
public class CustomValidatorImpl implements CustomValidator {

	@Setter(AccessLevel.PACKAGE)
	private Validator validator;

	public CustomValidatorImpl() {
		this.validator = new BeanValidator();
	}

	@Override
	public ValidationResult validate(Object object) {
		return ValidationResult.of(this.validator.validate(object));
	}

	@Override
	public <T> ValidationResult validate(T object, Class<? extends ValidationRule<? super T>> validationRuleClass) {
		return this.builder(object).with(validationRuleClass).validate();
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
