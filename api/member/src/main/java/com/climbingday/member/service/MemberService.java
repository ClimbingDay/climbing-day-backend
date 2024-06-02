package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.climbingday.domain.common.repository.RedisRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.member.EmailAuthDto;
import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.dto.member.MemberTokenDto;
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
	private final RedisRepository redisRepository;
	private final RestTemplate restTemplate;

	/**
	 * 회원 등록
	 */
	@Transactional
	public Long registerMember(MemberRegisterDto memberRegisterDto) {
		// 이메일 중복
		checkEmail(memberRegisterDto.getEmail());

		if(memberRepository.existsByPhoneNumber(memberRegisterDto.getPhoneNumber())){
			throw new MemberException(DUPLICATED_MEMBER_PHONE_NUMBER);
		}

		// 비밀번호 확인
		validatePassword(memberRegisterDto.getPassword(), memberRegisterDto.getPasswordConfirm());

		Member member = Member.fromMemberRegisterDto(memberRegisterDto);
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
		String refreshToken = jwtProvider.createRefreshToken(userDetail);
		int refreshExpirationSeconds = jwtProvider.getRefreshExpirationSeconds();

		MemberTokenDto loginTokenDto = MemberTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();

		redisRepository.setRedisRefreshToken(userDetail.getId(), refreshToken, refreshExpirationSeconds);

		return loginTokenDto;
	}

	/**
	 * 이메일 인증 요청
	 */
	public void emailAuth(EmailAuthDto emailAuthDto) {
		String email = emailAuthDto.getEmail();
		checkEmail(email);

		String authCode = generateCode();

		Map<String, String> emailInfo = new HashMap<>();
		emailInfo.put("email", email);
		emailInfo.put("authCode", authCode);

		sendEmailVerification(emailInfo);
	}

	/**
	 * 회원 조회
	 */
	public List<MemberDto> getAllMember() {
		return memberRepository.getAllMember();
	}

	/**
	 * email 중복체크
	 */
	private void checkEmail(String email) {
		if(memberRepository.existsByEmail(email)){
			throw new MemberException(DUPLICATED_MEMBER_EMAIL);
		}
	}

	/**
	 * password, passwordConfirm 체크
	 */
	private void validatePassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(PASSWORD_NOT_MATCHED);
		}
	}

	/**
	 * 이메일 인증코드 생성
	 */
	private String generateCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 숫자 생성
		return String.valueOf(code);
	}

	/**
	 * 이메일 인증코드 요청
	 */
	private void sendEmailVerification(Map<String, String> emailInfo) {
		String url = "http://localhost:8089/v1/mail/verification/send";
		restTemplate.postForObject(url, emailInfo, String.class);
	}
}
