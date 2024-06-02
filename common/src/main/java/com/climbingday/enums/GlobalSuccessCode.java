package com.climbingday.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GlobalSuccessCode {
	SUCCESS(200, "정상 처리되었습니다.", HttpStatus.OK),
	EMAIL_SEND_SUCCESS(200, "인증 메일이 정상적으로 발송되었습니다.", HttpStatus.OK),
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