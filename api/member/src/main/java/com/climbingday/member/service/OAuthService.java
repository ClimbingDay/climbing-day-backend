package com.climbingday.member.service;

import com.climbingday.domain.member.Member;
import com.climbingday.dto.member.MemberTokenDto;
import com.climbingday.dto.member.OAuthLoginDto;
import com.climbingday.dto.member.OAuthRegisterDto;

public interface OAuthService {
	MemberTokenDto login(OAuthLoginDto oAuthLoginDto);

	MemberTokenDto registerAndLogin(OAuthRegisterDto oAuthRegisterDto);
}
