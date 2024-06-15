package com.climbingday.center.exception;

import static com.climbingday.enums.GlobalErrorCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.climbingday.enums.BaseErrorCode;
import com.climbingday.response.ErrorResponse;
import com.climbingday.security.exception.CustomSecurityException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : {}", ex);
		return ResponseEntity.status(INTERNAL_SERVER_ERROR.getStatus())
			.body(INTERNAL_SERVER_ERROR.getErrorResponse());
	}

	@ExceptionHandler(CenterException.class)
	protected ResponseEntity<ErrorResponse> handleMemberException(CenterException ex) {
		log.error(">>>>> CenterException : {}", ex);
		BaseErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(CustomSecurityException.class)
	protected ResponseEntity<ErrorResponse> handleSecurityException(CustomSecurityException ex) {
		log.error(">>>>> SecurityException : {}", ex);
		BaseErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.error(">>>>> validation Failed : {}", ex);
		BindingResult bindingResult = ex.getBindingResult();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		ErrorResponse errorResponse = VALIDATION_FAILED.getErrorResponse();
		fieldErrors.forEach(error -> errorResponse.addValidation(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(VALIDATION_FAILED.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleMIssingRequestHeaderException(MissingRequestHeaderException ex) {
		log.error(">>>>> requestHeaderException : {}", ex);
		String headerName = ex.getHeaderName();

		ErrorResponse errorResponse = MISSING_REFRESH_REQUEST_HEADER.getErrorResponse();
		return ResponseEntity.status(MISSING_REFRESH_REQUEST_HEADER.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
		log.error(">>>>> MissingServletRequestParameterException : {}", ex);
		ErrorResponse errorResponse = MISSING_REQUEST_PARAM.getErrorResponse();

		String missingParam = ex.getParameterName();
		errorResponse.addMissingParams(missingParam, missingParam + " " + MISSING_REQUEST_PARAM.getErrorMessage());

		return ResponseEntity.status(MISSING_REQUEST_PARAM.getStatus()).body(errorResponse);
	}
}
