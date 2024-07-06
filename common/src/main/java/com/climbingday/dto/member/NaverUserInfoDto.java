package com.climbingday.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class NaverUserInfoDto {
	private String resultCode;

	private String message;

	@JsonProperty("response")
	private NaverResponseDto naverResponseDto;
}
