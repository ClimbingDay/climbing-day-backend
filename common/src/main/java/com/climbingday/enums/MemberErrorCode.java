package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import com.climbingday.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum MemberErrorCode implements BaseErrorCode {
	NOT_EXISTS_MEMBER(400, "존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST),
	DUPLICATED_MEMBER_EMAIL(409, "이미 등록된 회원 이메일입니다.", HttpStatus.CONFLICT),
	DUPLICATED_MEMBER_PHONE_NUMBER(409, "이미 등록된 휴대폰 번호입니다.", HttpStatus.CONFLICT),
	DUPLICATED_MEMBER_NICK_NAME(409, "이미 등록된 닉네임입니다.", HttpStatus.CONFLICT),
	DELETE_MEMBER(400, "탈퇴 또는 삭제된 회원입니다.", HttpStatus.BAD_REQUEST),
	CHECK_ID_OR_PASSWORD(401, "아이디 또는 비밀번호를 확인해주세요.", HttpStatus.UNAUTHORIZED),
	NOT_MATCHED_PASSWORD(400, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
	NO_UPDATE_FIELDS(400, "업데이트할 항목이 없습니다.", HttpStatus.BAD_REQUEST),
	NOT_EXISTS_POST(400, "존재하지 않는 게시글 입니다.", HttpStatus.BAD_REQUEST),
	DISAGREE_REQUIRED_TERMS(400, "필수 이용약관에 동의하지 않았습니다.", HttpStatus.BAD_REQUEST),
	NOT_FIND_TERMS(400, "해당 약관을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
	UNSUPPORTED_OAUTH_LOGIN(400, "지원하지 않는 소셜로그인입니다.", HttpStatus.BAD_REQUEST),
	NAVER_LOGIN_ERROR(504, "네이버 로그인이 정상적으로 처리되지 않았습니다.", HttpStatus.GATEWAY_TIMEOUT);

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
