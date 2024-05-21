package com.climbingday.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberTokenDto {
	private String accessToken;

	private String refreshToken;
}
