package br.com.nzz.validation.impl;

import br.com.nzz.validation.ObjectValidator;
import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.ValidationResult;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

/**
 * @param <T> type of the validator
 * @author Luiz.Nazari
 */
public class ObjectValidatorImpl<T> implements ObjectValidator<T> {

	private final List<ValidationBlock<T>> validationBlocks = Lists.newLinkedList();

	ObjectValidatorImpl() {
		this.finalizeCurrentValidationBlock();
	}

	@Override
	public ValidationResult validate(T object) {
		for (ValidationBlock<T> block : this.validationBlocks) {
			Set<ValidationError> validationErrors = block.execute(object);
			if (!validationErrors.isEmpty()) {
				return ValidationResult.ofErrors(validationErrors);
			}
		}

		return ValidationResult.empty();
	}

	ValidationBlock<T> currentValidationBlock() {
		return Iterables.getLast(this.validationBlocks);
	}

	void finalizeCurrentValidationBlock() {
		ValidationBlock<T> validationBlock = new ValidationBlock<>();
		validationBlocks.add(validationBlock);
	}

}
