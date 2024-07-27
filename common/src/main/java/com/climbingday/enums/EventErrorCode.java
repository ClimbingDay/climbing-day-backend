package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum EventErrorCode implements BaseErrorCode {
	UNABLE_TO_SEND_EMAIL(504, "이메일 전송에 실패했습니다.", HttpStatus.GATEWAY_TIMEOUT),
	NOT_MATCHED_AUTH_CODE(401, "인증 코드가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
	NOT_AUTHENTICATED_EMAIL(400, "이메일 인증이 되지 않았습니다. 이메일 인증 후 다시 시도해 주세요.", HttpStatus.BAD_REQUEST),
	NOT_FIND_MATCHING_MEMBER(404, "현재 위치에 매칭 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	NOT_EXISTS_CENTER_LEVEL(400, "존재하지 않는 레벨이 포함되어 있습니다.", HttpStatus.BAD_REQUEST);


	private final int errorCode;
	private final String errorMessage;
	private final HttpStatus status;

	EventErrorCode(int errorCode, String message, HttpStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.status = status;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(this.errorCode, this.errorMessage);
	}
}
