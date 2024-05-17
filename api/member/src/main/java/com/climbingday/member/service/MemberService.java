package com.climbingday.member.service;

import static com.climbingday.domain.common.enums.MemberErrorCode.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.member.dto.MemberLoginDto;
import com.climbingday.member.dto.MemberRegisterDto;
import com.climbingday.member.dto.MemberTokenDto;
import com.climbingday.member.exception.MemberException;
import com.climbingday.security.jwt.JwtProvider;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	/**
	 * 회원 등록
	 */
	@Transactional
	public Long registerMember(MemberRegisterDto memberRegisterDto) {
		// 아이디 중복 체크
		if(memberRepository.existsByEmail(memberRegisterDto.getEmail())){
			throw new MemberException(DUPLICATED_MEMBER_EMAIL);
		}else if(memberRepository.existsByPhoneNumber(String.join("-", memberRegisterDto.getPhoneNumber()))){
			throw new MemberException(DUPLICATED_MEMBER_PHONE_NUMBER);
		}

		// 비밀번호 확인
		validatePassword(memberRegisterDto.getPassword(), memberRegisterDto.getPasswordConfirm());

		Member member = MemberRegisterDto.toMember(memberRegisterDto);
		member.setPassword(passwordEncoder.encode(memberRegisterDto.getPassword()));

		return memberRepository.save(member).getId();
	}

	/**
	 * 회원 로그인
	 */
	public MemberTokenDto login(MemberLoginDto memberLogindto) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			memberLogindto.getEmail(),
			memberLogindto.getPassword()
		);

		Authentication authenticated = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetail = (UserDetailsImpl) authenticated.getPrincipal();

		String accessToken = jwtProvider.createAccessToken(userDetail);

		MemberTokenDto loginTokenDto = MemberTokenDto.builder()
			.accessToken(accessToken)
			.build();

		return loginTokenDto;
	}


	/**
	 * password, passwordConfirm 체크
	 */
	private void validatePassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(PASSWORD_NOT_MATCHED);
		}
	}
}
