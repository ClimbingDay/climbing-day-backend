package com.climbingday.response;

import static com.climbingday.enums.GlobalSuccessCode.*;

import com.climbingday.enums.GlobalSuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class CDResponse<T> {
	private int code;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public CDResponse(GlobalSuccessCode statusCode) {
		this.code = statusCode.getCode();
		this.message = statusCode.getMessage();
	}

	public CDResponse(T data) {
		this.code = SUCCESS.getCode();
		this.message = SUCCESS.getMessage();
		this.data = data;
	}

	public CDResponse(GlobalSuccessCode statusCode, T data) {
		this.code = statusCode.getCode();
		this.message = statusCode.getMessage();
		this.data = data;
	}
}
