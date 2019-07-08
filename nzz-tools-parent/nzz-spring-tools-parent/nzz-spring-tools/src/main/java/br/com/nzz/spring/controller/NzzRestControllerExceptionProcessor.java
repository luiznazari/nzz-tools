package br.com.nzz.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import br.com.nzz.spring.exception.ApiErrorMessage;
import br.com.nzz.spring.exception.WebServiceEntityNotFoundException;
import br.com.nzz.spring.NzzConstants;
import br.com.nzz.spring.exception.WebServiceException;
import br.com.nzz.validation.exception.CustomValidationException;
import br.com.nzz.validation.message.ErrorMessage;
import lombok.extern.log4j.Log4j2;


/**
 * Classe responsável por interceptar todas as exception geradas em classes
 * {@link RestController} e transformar em um Response compativel possuindo o
 * código correto do erro e a mensagem traduzida para JSON.
 *
 * @author Luiz Felipe Nazari
 */
@Log4j2
@RestControllerAdvice
public class NzzRestControllerExceptionProcessor {

	/**
	 * Intercepta todos os erros padrões de serviços WebService.
	 *
	 * @param exception WebServiceException
	 * @return JSON {@link ApiErrorMessage}
	 */
	@ExceptionHandler(WebServiceException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(WebServiceException exception) {
		log.trace(() -> exception.getError().getMessage(), exception);
		return exception.getError()
			.asErrorList();
	}

	/**
	 * Intercepta todos os erros de entidades não encontradas.
	 *
	 * @param exception WebServiceEntityNotFoundException
	 * @return JSON {@link ApiErrorMessage}
	 */
	@ExceptionHandler(WebServiceEntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(WebServiceEntityNotFoundException exception) {
		return exception.getError()
			.asErrorList();
	}

	/**
	 * Intercepta todos os erros padrões de validações de serviços.
	 *
	 * @param exception CustomValidationException
	 * @return JSON {@link br.com.nzz.validation.ValidationError}
	 */
	@ExceptionHandler(CustomValidationException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(CustomValidationException exception) {
		return exception.getErrors()
			.stream()
			.map(ApiErrorMessage::from)
			.collect(Collectors.toList());
	}

	/**
	 * Intercepta todos os erros de conversão de objetos recebidos em
	 * requisições dos controladores.
	 *
	 * @param exception {@link HttpMessageConversionException}
	 * @return JSON {@link br.com.nzz.validation.ValidationError}
	 */
	@ExceptionHandler(HttpMessageConversionException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(HttpMessageConversionException exception) {
		log.error(exception.getMessage(), exception);
		return new ApiErrorMessage(NzzConstants.INVALID_JSON_ERROR, exception.getMessage())
			.asErrorList();
	}

	/**
	 * Intercepta os erros de processamento de JSON.
	 *
	 * @param exception {@link JsonProcessingException}
	 * @return JSON {@link ApiErrorMessage}
	 */
	@ExceptionHandler(JsonProcessingException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(JsonProcessingException exception) {
		log.error(exception.getMessage(), exception);
		return new ApiErrorMessage(NzzConstants.INVALID_JSON_ERROR, exception.getMessage())
			.asErrorList();
	}

	/**
	 * Intercepta todos os erros de método não suportado.
	 *
	 * @param exception {@link HttpRequestMethodNotSupportedException}
	 * @return JSON {@link ApiErrorMessage}
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(HttpRequestMethodNotSupportedException exception) {
		return new ApiErrorMessage(NzzConstants.INVALID_REQUEST_ERROR, exception.getMessage())
			.asErrorList();
	}

	/**
	 * Intercepta todos os erros de parâmetros não informados.
	 *
	 * @param exception {@link ServletRequestBindingException}
	 * @return JSON {@link ApiErrorMessage}
	 */
	@ExceptionHandler(ServletRequestBindingException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(ServletRequestBindingException exception) {
		log.error(exception.getMessage(), exception);
		return new ApiErrorMessage(NzzConstants.INVALID_REQUEST_ERROR, exception.getMessage())
			.asErrorList();
	}

	/**
	 * Intercepta todos os erros não tratados lançados pela aplicação.
	 *
	 * @param exception {@link Exception}
	 * @return JSON {@link ApiErrorMessage}
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public List<ErrorMessage> exceptionRule(Exception exception) {
		log.error(exception.getMessage(), exception);
		return new ApiErrorMessage(NzzConstants.INTERNAL_ERROR, exception.getMessage())
			.asErrorList();
	}

}
