package com.climbingday.member.dto;

import lombok.Data;

@Data
public class MemberLoginDto {
	private String accessToken;

	private String refreshToken;
}
