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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class FrescoRestControllerAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FrescoRestControllerAdvice.class);

	private final MessageSource messageSource;

	private final Locale currentLocale = LocaleContextHolder.getLocale();

	@Autowired
	public FrescoRestControllerAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptionMethod(final MethodArgumentNotValidException ex, final WebRequest requset) {
		log.info("*** [BotRestControllerAdvice] :: handleAllExceptionMethod : Exception : {} ***", ex.getMessage());
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
		String errorMessage = ErrorCodes.FK_EXCEPTION.name();
		try {
			 errorMessage = messageSource.getMessage(exc.getMessage(), exc.getArgs(), currentLocale);
		}catch (Exception e) {
			LOGGER.info("Rest Controller Advice Exception {}",errorMessage);
		}
		return ResponseEntity.status(Objects.nonNull(exc.getStatus()) ? exc.getStatus() : HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(exc.getStatus().value(), exc.getStatus().getReasonPhrase(), errorMessage,
						((ServletWebRequest) requset).getRequest().getServletPath(), exc.getCode(), exc.getMessage()));
	}


	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex, final WebRequest requset) {
		log.info("[BotRestControllerAdvice] :: handleMethodArgumentTypeMismatchException : Exception : {}", ex.getMessage());
		ExceptionResponse exceptionMessageObj = new ExceptionResponse();
		exceptionMessageObj.setStatus(HttpStatus.BAD_REQUEST.value());
		exceptionMessageObj.setCode(ErrorCodes.NOT_NULL_VALIDATION.getValue());
		exceptionMessageObj.setMessage(ex.getMessage());
		exceptionMessageObj.setError(ex.getClass().getCanonicalName());
		exceptionMessageObj.setPath(((ServletWebRequest) requset).getRequest().getServletPath());
		return new ResponseEntity<>(exceptionMessageObj, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

}
