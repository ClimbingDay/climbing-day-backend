package com.climbingday.security.service;

import static com.climbingday.enums.MemberErrorCode.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.climbingday.security.exception.CustomSecurityException;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final MemberUserDetailsService memberUserDetailsService;
	private final PasswordEncoder passwordEncoder;

	public CustomAuthenticationProvider(
		@Qualifier("memberUserDetailsService") MemberUserDetailsService memberUserDetailsService,
		PasswordEncoder passwordEncoder) {
		this.memberUserDetailsService = memberUserDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetailsImpl userDetails;

		userDetails = (UserDetailsImpl)memberUserDetailsService.loadUserByUsername(email);
		
		// 비밀번호 확인
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new CustomSecurityException(CHECK_ID_OR_PASSWORD);
		}

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
