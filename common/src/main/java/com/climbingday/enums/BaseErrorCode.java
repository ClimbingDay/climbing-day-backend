package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

public interface BaseErrorCode {
	int getErrorCode();

	String getErrorMessage();

	HttpStatus getStatus();

	ErrorResponse getErrorResponse();
}
