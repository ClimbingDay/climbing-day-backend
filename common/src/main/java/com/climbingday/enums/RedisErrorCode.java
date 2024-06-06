package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum RedisErrorCode implements BaseErrorCode {
	NOT_EXIST_EMAIL_INFO(400, "이메일 인증 정보가 확인되지 않습니다. 확인 후 다시 시도해주세요.", HttpStatus.BAD_REQUEST),
	REDIS_EMPTY_KEY(500, "처리 중 에러가 발생했습니다. 정상적인 방법으로 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final int errorCode;
	private final String errorMessage;
	private final HttpStatus status;

	RedisErrorCode(int errorCode, String message, HttpStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.status = status;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(this.errorCode, this.errorMessage);
	}
}
