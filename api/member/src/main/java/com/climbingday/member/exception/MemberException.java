package com.climbingday.member.exception;

import com.climbingday.enums.BaseErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
	private final BaseErrorCode errorCode;

	public MemberException(BaseErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
