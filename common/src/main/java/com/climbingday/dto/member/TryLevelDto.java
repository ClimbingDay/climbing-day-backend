package com.climbingday.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class TryLevelDto {
	@NotNull(message = "시도한 레벨 아이디는 필수 항목입니다.")
	private Long tryLevelId;

	@NotNull(message = "성공 여부는 필수 항목입니다.")
	@JsonProperty("isSuccess")
	private boolean isSuccess;
}
