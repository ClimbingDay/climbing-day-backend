package com.climbingday.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoubleValidator implements ConstraintValidator<DoubleValid, Double> {
	@Override
	public void initialize(DoubleValid constraintAnnotation) {
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // null을 유효하게 처리하거나, false를 반환하여 null을 무효처리
		}

		// 실수 값이 특정 범위 내에 있는지 확인 (예: 0.0에서 100.0 사이)
		return value >= -1000.0 && value <= 10000.0;
	}
}
