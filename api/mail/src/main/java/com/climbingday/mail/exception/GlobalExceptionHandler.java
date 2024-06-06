package com.climbingday.mail.exception;

import static com.climbingday.enums.GlobalErrorCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.climbingday.enums.MailErrorCode;
import com.climbingday.response.ErrorResponse;

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

	@ExceptionHandler(MailException.class)
	protected ResponseEntity<ErrorResponse> handleMemberException(MailException ex) {
		log.error(">>>>> MailException : {}", ex);
		MailErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(errorCode.getErrorResponse());
	}
}
