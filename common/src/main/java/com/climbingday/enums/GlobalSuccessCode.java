package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GlobalSuccessCode {
	SUCCESS(200, "정상적으로 처리되었습니다.", HttpStatus.OK),
	PASSWORD_RESET_SUCCESS(200, "비밀번호 변경이 정상적으로 처리되었습니다.", HttpStatus.OK),
	EMAIL_SEND_SUCCESS(200, "인증 메일이 정상적으로 발송되었습니다.", HttpStatus.OK),
	MEMBER_PROFILE_UPDATE_SUCCESS(200, "프로필 수정이 정상적으로 처리되었습니다.", HttpStatus.OK),
	EMAIL_AUTH_CONFIRM(200, "이메일 인증이 정상적으로 처리되었습니다.", HttpStatus.OK),
	AVAILABLE_EMAIL(200, "사용이 가능한 이메일입니다.", HttpStatus.OK),
	AVAILABLE_PHONE_NUM(200, "사용이 가능한 휴대폰번호입니다.", HttpStatus.OK),
	AVAILABLE_NICK_NAME(200, "사용이 가능한 닉네임입니다.", HttpStatus.OK),
	CREATE(201, "정상적으로 생성되었습니다.", HttpStatus.CREATED);

	private final int code;
	private final String message;
	private final HttpStatus status;

	GlobalSuccessCode(int code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
