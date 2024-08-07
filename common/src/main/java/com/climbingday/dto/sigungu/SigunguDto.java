package com.climbingday.dto.sigungu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class SigunguDto {
	private String code;

	private String name;
}
