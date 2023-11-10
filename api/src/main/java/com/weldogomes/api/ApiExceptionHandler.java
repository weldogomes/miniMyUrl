package com.weldogomes.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.weldogomes.api.url.business.UrlBusinessException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(UrlBusinessException.class)
	public ResponseEntity<String> handleUrlBusinessException(UrlBusinessException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
