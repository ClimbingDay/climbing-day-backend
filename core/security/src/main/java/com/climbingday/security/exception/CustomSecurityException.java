package com.climbingday.security.exception;

import com.climbingday.domain.common.enums.BaseErrorCode;

import lombok.Getter;

@Getter
public class CustomSecurityException extends RuntimeException {
	private final BaseErrorCode errorCode;

	public CustomSecurityException(BaseErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}