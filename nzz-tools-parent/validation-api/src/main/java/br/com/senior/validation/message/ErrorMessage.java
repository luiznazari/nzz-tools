package br.com.senior.validation.message;

import java.io.Serializable;

/**
 * An error containing the error code (or error key) and the error message.
 *
 * @author Luiz.Nazari
 */
public interface ErrorMessage extends Serializable {

	/**
	 * @return the error message key
	 */
	String getMessageKey();

	/**
	 * @return the error, user friendly, message
	 */
	String getMessage();

}
