package com.climbingday.dto.record;

import com.climbingday.annotation.LocalDateTimeValid;

import jakarta.validation.constraints.NotNull;
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
	private String startTime;

	@LocalDateTimeValid
	private String endTime;

	@NotNull(message = "기록시간은 필수 항목입니다.")
	private Long duration;
}
