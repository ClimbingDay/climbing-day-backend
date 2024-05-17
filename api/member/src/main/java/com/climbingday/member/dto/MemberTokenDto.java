package com.climbingday.member.dto;

import lombok.Data;

@Data
public class MemberTokenDto {
	private String accessToken;

	private String refreshToken;
}
