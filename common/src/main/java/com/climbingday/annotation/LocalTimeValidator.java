package com.climbingday.annotation;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalTimeValidator implements ConstraintValidator<LocalTimeValid, LocalTime> {
	@Override
	public void initialize(LocalTimeValid constraintAnnotation) {
	}

	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // null을 유효하게 처리하거나, false를 반환하여 null을 무효처리
		}

		// 시간 검사 로직 (예: 특정 시간 범위 내에 있는지 확인)
		return !value.isBefore(LocalTime.of(0, 0)) && !value.isAfter(LocalTime.of(23, 59));
	}
}
