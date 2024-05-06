package com.climbingday.domain.member.repository;

import static com.climbingday.domain.common.enums.GlobalSuccessCode.*;

import com.climbingday.domain.common.enums.GlobalSuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class CResponse<T> {
	private int code;
	private String message;
	private T data;

	public CResponse(T data) {
		this.code = SUCCESS.getCode();
		this.message = SUCCESS.getMessage();
		this.data = data;
	}

	public CResponse(GlobalSuccessCode statusCode, T data) {
		this.code = statusCode.getCode();
		this.message = statusCode.getMessage();
		this.data = data;
	}
}
