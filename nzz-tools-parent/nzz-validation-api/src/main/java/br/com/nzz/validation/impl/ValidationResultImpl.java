package br.com.nzz.validation.impl;


import br.com.nzz.validation.exception.CustomValidationException;
import br.com.nzz.validation.ValidationError;
import br.com.nzz.validation.ValidationResult;
import lombok.SneakyThrows;

import com.google.common.collect.Sets;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Luiz.Nazari
 */
public class ValidationResultImpl implements ValidationResult {

	private final Set<ValidationError> validationErrors = Sets.newLinkedHashSet();
	private final Set<ConstraintViolation<?>> constraintViolations;

	public ValidationResultImpl(Set<? extends ConstraintViolation<?>> constraintViolations, Set<? extends ValidationError> errors) {
		this.constraintViolations = Collections.unmodifiableSet(
			Optional.ofNullable(constraintViolations).orElse(Collections.emptySet()));
		this.validationErrors.addAll(errors);
	}

	@Override
	public ValidationResult validate(Supplier<? extends ValidationError> errorSupplier) {
		try {
			ValidationError validationError = errorSupplier.get();
			if (validationError != null) {
				this.validationErrors.add(validationError);
			}
		} catch (CustomValidationException e) {
			this.validationErrors.addAll(e.getErrors());
		}
		return this;
	}

	@Override
	public ValidationResult onErrorThrowException() {
		this.onError(errors -> {
			throw new CustomValidationException(errors);
		});
		return this;
	}

	@Override
	public <X extends Throwable> ValidationResult onErrorThrow(Supplier<X> throwableSupplier) throws X {
		if (this.hasErrors()) {
			throw throwableSupplier.get();
		}
		return this;
	}

	@Override
	public <X extends Throwable> ValidationResult onErrorThrow(Function<Set<? extends ValidationError>, X> throwableFunction) throws X {
		if (this.hasErrors()) {
			throw throwableFunction.apply(this.getErrors());
		}
		return this;
	}

	@Override
	public ValidationResult onError(Consumer<Set<? extends ValidationError>> consumer) {
		if (this.hasErrors()) {
			consumer.accept(this.getErrors());
		}
		return this;
	}

	@Override
	public ValidationResult ifNoError(Runnable runnable) {
		if (!this.hasErrors()) {
			runnable.run();
		}
		return this;
	}

	@Override
	public boolean hasErrors() {
		return !this.constraintViolations.isEmpty() || !this.validationErrors.isEmpty();
	}

	@Override
	public Set<ValidationError> getErrors() {
		Set<ValidationError> errors = Sets.newHashSet();
		errors.addAll(this.validationErrors);
		errors.addAll(this.constraintViolations.stream()
			.map(ConstraintViolationError::new).collect(Collectors.toList()));
		return Collections.unmodifiableSet(errors);
	}


}
