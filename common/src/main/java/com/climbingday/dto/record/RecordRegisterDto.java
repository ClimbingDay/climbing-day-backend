package com.climbingday.dto.record;

import java.time.LocalDateTime;

import com.climbingday.annotation.LocalDateTimeValid;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordRegisterDto {

	@LocalDateTimeValid
	private LocalDateTime startTime;

	@LocalDateTimeValid
	private LocalDateTime endTime;

	@NotBlank(message = "기록시간은 필수 항목입니다.")
	private Long duration;
}
