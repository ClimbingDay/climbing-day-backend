package com.climbingday.center.exception;

import com.climbingday.enums.BaseErrorCode;

import lombok.Getter;

@Getter
public class CenterException extends RuntimeException {
	private final BaseErrorCode errorCode;

	public CenterException(BaseErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
