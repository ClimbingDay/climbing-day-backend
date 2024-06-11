package com.climbingday.annotation;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalTimeValidator implements ConstraintValidator<LocalTimeValid, String> {
	@Override
	public void initialize(LocalTimeValid constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // null을 유효하게 처리하거나, false를 반환하여 null을 무효처리
		}

		// "0:0" 형식을 "00:00" 형식으로 변환
		String[] parts = value.split(":");
		if (parts.length == 2) {
			parts[0] = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
			parts[1] = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
			value = parts[0] + ":" + parts[1];
		}

		// 시간 검사 로직 (예: 특정 시간 범위 내에 있는지 확인)
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime time = LocalTime.parse(value, formatter);

			// 00:00부터 23:59 사이의 시간인지 확인
			LocalTime start = LocalTime.of(0, 0);
			LocalTime end = LocalTime.of(23, 59);
			return !time.isBefore(start) && !time.isAfter(end);
		} catch (DateTimeParseException e) {
			return false; // 형식이 잘못된 경우
		}
	}
}
