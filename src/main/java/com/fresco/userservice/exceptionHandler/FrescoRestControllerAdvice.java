package com.fresco.userservice.exceptionHandler;


import com.fresco.userservice.exceptionHandler.enums.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class FrescoRestControllerAdvice extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	private final Locale currentLocale = LocaleContextHolder.getLocale();

	@Autowired
	public FrescoRestControllerAdvice(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptionMethod(final MethodArgumentNotValidException ex, final WebRequest requset) {
		log.info("*** [FrescoRestControllerAdvice] :: handleAllExceptionMethod : Exception : {} ***", ex.getMessage());
		ExceptionResponse exceptionMessageObj = new ExceptionResponse();
		StringBuilder sb = new StringBuilder();
		List<FieldError> fieldErrors = (ex).getBindingResult().getFieldErrors();
		if(!fieldErrors.isEmpty()) {
			sb.append(fieldErrors.get(0).getDefaultMessage());
		}
		exceptionMessageObj.setStatus(HttpStatus.BAD_REQUEST.value());
		exceptionMessageObj.setCode(ErrorCodes.NOT_NULL_VALIDATION.getValue());
		exceptionMessageObj.setMessage(sb.toString());
		exceptionMessageObj.setError(ex.getClass().getCanonicalName());
		exceptionMessageObj.setPath(((ServletWebRequest) requset).getRequest().getServletPath());
		exceptionMessageObj.setType(ErrorCodes.INVALID_DATA.name());
		return new ResponseEntity<>(exceptionMessageObj, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ExceptionResponse> handleBusinessException(final BusinessException exc,final WebRequest requset) {
		String errorMessage = messageSource.getMessage(exc.getMessage(), exc.getArgs(), currentLocale);
		return ResponseEntity.status(Objects.nonNull(exc.getStatus()) ? exc.getStatus() : HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(exc.getStatus().value(), exc.getStatus().getReasonPhrase(), errorMessage,
						((ServletWebRequest) requset).getRequest().getServletPath(), exc.getCode(), exc.getMessage()));
	}

}
