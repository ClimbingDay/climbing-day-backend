package com.climbingday.annotation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalDateTimeValidator implements ConstraintValidator<LocalDateTimeValid, String> {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public void initialize(LocalDateTimeValid constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return false; // null을 유효하게 처리하거나, false를 반환하여 null을 무효처리
		}

		try {
			// "0:0" 형식을 "00:00" 형식으로 변환
			String[] parts = value.split(" ");
			if (parts.length == 2) {
				String datePart = parts[0];
				String timePart = parts[1];
				String[] timeParts = timePart.split(":");
				if (timeParts.length == 3) {
					timeParts[0] = timeParts[0].length() == 1 ? "0" + timeParts[0] : timeParts[0];
					timeParts[1] = timeParts[1].length() == 1 ? "0" + timeParts[1] : timeParts[1];
					timeParts[2] = timeParts[2].length() == 1 ? "0" + timeParts[2] : timeParts[2];
					value = datePart + " " + timeParts[0] + ":" + timeParts[1] + ":" + timeParts[2];
				}
			}

			LocalDateTime dateTime = LocalDateTime.parse(value, formatter);

			// 00:00:00부터 23:59:59 사이의 시간인지 확인
			LocalTime start = LocalTime.of(0, 0, 0);
			LocalTime end = LocalTime.of(23, 59, 59);
			LocalTime time = dateTime.toLocalTime();

			// 현재 날짜 이후인지 확인 (시간 무시)
			LocalDate today = LocalDate.now();
			if(dateTime.toLocalDate().isBefore(today)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("현재 년월일 이후만 입력 가능합니다.").addConstraintViolation();
				return false;
			}

			return !time.isBefore(start) && !time.isAfter(end);

		}catch(DateTimeParseException e) {
			return false;
		}
	}
}
