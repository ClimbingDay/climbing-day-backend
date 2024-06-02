package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum MailErrorCode implements BaseErrorCode {
	UNABLE_TO_SEND_EMAIL(400, "이메일 전송에 실패했습니다.", HttpStatus.BAD_REQUEST);

	private final int errorCode;
	private final String errorMessage;
	private final HttpStatus status;

	MailErrorCode(int errorCode, String message, HttpStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.status = status;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(this.errorCode, this.errorMessage);
	}
}
