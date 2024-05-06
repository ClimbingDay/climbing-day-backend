package com.climbingday.domain.common.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.domain.response.ErrorResponse;

public interface BaseErrorCode {
	int getErrorCode();

	String getErrorMessage();

	HttpStatus getStatus();

	ErrorResponse getErrorResponse();
}
