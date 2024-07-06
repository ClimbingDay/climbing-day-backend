package com.climbingday.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class NaverResponseDto {
	private String id;

	private String name;

	private String email;

	private String birthday;

	private String birthyear;

	@JsonProperty("mobile")
	private String phoneNumber;
}
