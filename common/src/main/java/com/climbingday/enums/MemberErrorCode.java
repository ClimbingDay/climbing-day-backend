package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum MemberErrorCode implements BaseErrorCode {
	DUPLICATED_MEMBER_EMAIL(409, "이미 존재하는 회원 이메일입니다.", HttpStatus.CONFLICT),
	DUPLICATED_MEMBER_PHONE_NUMBER(409, "이미 등록된 휴대폰 번호입니다.", HttpStatus.CONFLICT),
	DELETE_MEMBER(400, "탈퇴 또는 삭제된 회원입니다.", HttpStatus.BAD_REQUEST),
	CHECK_ID_OR_PASSWORD(401, "아이디 또는 비밀번호를 확인해주세요.", HttpStatus.UNAUTHORIZED),
	PASSWORD_NOT_MATCHED(400, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
	UNABLE_TO_SEND_EMAIL(504, "이메일 전송에 실패했습니다.", HttpStatus.GATEWAY_TIMEOUT);

	private final int errorCode;
	private final String errorMessage;
	private final HttpStatus status;

	MemberErrorCode(int errorCode, String message, HttpStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.status = status;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(this.errorCode, this.errorMessage);
	}
}
