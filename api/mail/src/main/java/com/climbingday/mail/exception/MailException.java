package com.climbingday.mail.exception;

import com.climbingday.enums.MailErrorCode;

import lombok.Getter;

@Getter
public class MailException extends RuntimeException {
	private final MailErrorCode errorCode;

	public MailException(MailErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
