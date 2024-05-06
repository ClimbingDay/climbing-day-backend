package com.climbingday.member.exception;

import com.climbingday.domain.common.enums.MemberErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
	private final MemberErrorCode errorCode;

	public MemberException(MemberErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
