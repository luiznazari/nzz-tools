package br.com.senior.validation.impl;

import br.com.senior.validation.ValidationError;
import lombok.Getter;
import lombok.ToString;

import javax.validation.ConstraintViolation;

/**
 * @author Luiz.Nazari
 */
@Getter
@ToString
public class ConstraintViolationError implements ValidationError {

	private static final long serialVersionUID = 1454564994901855334L;

	private final String message;
	private final String messageKey;
	private final String reference;

	public ConstraintViolationError(ConstraintViolation<?> constraintViolation) {
		this.message = constraintViolation.getMessage();
		this.messageKey = extractMessageKey(constraintViolation.getMessageTemplate());
		this.reference = constraintViolation.getPropertyPath().toString();
	}

}
