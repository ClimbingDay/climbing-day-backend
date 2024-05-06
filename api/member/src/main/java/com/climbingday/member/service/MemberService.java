package com.climbingday.member.service;

import static com.climbingday.domain.common.enums.MemberErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.member.dto.request.MemberRegisterDto;
import com.climbingday.member.exception.MemberException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 회원 등록
	 */
	@Transactional
	public Long registerMember(MemberRegisterDto memberRegisterDto) {
		// 아이디 중복 체크
		if(memberRepository.existsByEmail(memberRegisterDto.getEmail())){
			throw new MemberException(DUPLICATED_MEMBER_EMAIL);
		}else if(memberRepository.existsByPhoneNumber(memberRegisterDto.getPhoneNumber())){
			throw new MemberException(DUPLICATED_MEMBER_PHONE_NUMBER);
		}

		// 비밀번호 확인
		validatePassword(memberRegisterDto.getPassword(), memberRegisterDto.getPasswordConfirm());

		Member member = MemberRegisterDto.toMember(memberRegisterDto);
		member.setPassword(passwordEncoder.encode(memberRegisterDto.getPassword()));

		return memberRepository.save(member).getId();
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
