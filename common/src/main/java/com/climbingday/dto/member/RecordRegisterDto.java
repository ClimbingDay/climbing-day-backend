package com.climbingday.dto.member;

import java.util.ArrayList;
import java.util.List;

import com.climbingday.annotation.LocalDateTimeValid;

import jakarta.validation.Valid;
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
	@NotNull(message = "암장 아이디는 필수 항목입니다.")
	private Long centerId;

	@LocalDateTimeValid
	private String startTime;

	@LocalDateTimeValid
	private String endTime;

	@NotNull(message = "기록시간은 필수 항목입니다.")
	private Long duration;

	@Valid
	private List<TryLevelDto> trylevelList = new ArrayList<>();
}
