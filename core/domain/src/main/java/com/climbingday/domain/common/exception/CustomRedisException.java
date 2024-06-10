package com.climbingday.domain.common.exception;

import com.climbingday.enums.BaseErrorCode;

import lombok.Getter;

@Getter
public class CustomRedisException extends RuntimeException {
	private final BaseErrorCode errorCode;

	public CustomRedisException(BaseErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
