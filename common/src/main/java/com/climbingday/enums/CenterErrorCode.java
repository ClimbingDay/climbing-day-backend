package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum CenterErrorCode implements BaseErrorCode {
	DUPLICATED_CENTER_NAME(409, "이미 암장이름입니다.", HttpStatus.CONFLICT),
	NOT_EXISTS_CENTER(400, "존재하지 않는 암장입니다.", HttpStatus.BAD_REQUEST);

	private final int errorCode;
	private final String errorMessage;
	private final HttpStatus status;

	CenterErrorCode(int errorCode, String message, HttpStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.status = status;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(this.errorCode, this.errorMessage);
	}
}
