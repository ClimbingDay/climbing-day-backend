package com.climbingday.dto.member;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberLocationDto {
	@NotNull(message = "위도는 필수 항목입니다.")
	private double latitude;

	@NotNull(message = "경도는 필수 항목입니다.")
	private double longitude;
}
