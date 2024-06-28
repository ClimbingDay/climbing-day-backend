package com.climbingday.event.exception;

import com.climbingday.enums.BaseErrorCode;
import com.climbingday.enums.EventErrorCode;

import lombok.Getter;

@Getter
public class EventException extends RuntimeException {
	private final BaseErrorCode errorCode;

	public EventException(BaseErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
}
