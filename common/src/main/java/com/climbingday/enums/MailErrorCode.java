package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum MailErrorCode implements BaseErrorCode {
	UNABLE_TO_SEND_EMAIL(504, "이메일 전송에 실패했습니다.", HttpStatus.GATEWAY_TIMEOUT),
	NOT_MATCHED_AUTH_CODE(401, "인증 코드가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);


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
