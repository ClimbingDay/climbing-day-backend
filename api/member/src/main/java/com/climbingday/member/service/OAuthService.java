package com.climbingday.member.service;

import com.climbingday.dto.member.MemberTokenDto;
import com.climbingday.dto.member.OAuthLoginDto;

public interface OAuthService {
	MemberTokenDto login(OAuthLoginDto oAuthLoginDto);
}
