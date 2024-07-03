package com.climbingday.member.service;

import static com.climbingday.enums.EventErrorCode.*;
import static com.climbingday.enums.GlobalErrorCode.*;
import static com.climbingday.enums.MemberErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.climbingday.domain.common.repository.RedisRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.member.EmailAuthDto;
import com.climbingday.dto.member.EmailDto;
import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberMyPageDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.dto.member.MemberTokenDto;
import com.climbingday.dto.member.PasswordResetDto;
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

	@Value("${service-url}")
	private String serviceUrl;

	/**
	 * 회원 등록
	 */
	@Transactional
	public Long registerMember(MemberRegisterDto memberRegisterDto) {
		checkEmail(memberRegisterDto.getEmail());														// 이메일 중복 확인
		validatePassword(memberRegisterDto.getPassword(), memberRegisterDto.getPasswordConfirm());		// 비밀번호 일치 확인
		checkPhoneNumber(memberRegisterDto.getPhoneNumber());											// 휴대폰 중복 확인
		checkNickName(memberRegisterDto.getNickName());													// 닉네임 중복 확인

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

		MemberTokenDto tokenDto = createToken(userDetail);
		int refreshExpirationSeconds = jwtProvider.getRefreshExpirationSeconds();
		redisRepository.setRedisRefreshToken(userDetail.getId(), tokenDto.getRefreshToken(), refreshExpirationSeconds);

		return tokenDto;
	}

	/**
	 * 비밀번호 재설정
	 */
	public void passwordReset(PasswordResetDto passwordResetDto) {
		String email = passwordResetDto.getEmail();

		if(emailAuthCheck(email)) {
			Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

			member.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
			memberRepository.save(member);
		}
	}

	/**
	 * 이메일 인증 여부 체크
	 */
	public boolean emailAuthCheck(String email) {
		Map emailVerificationInfo = redisRepository.getEmailCodeAndConfirm(email);

		String confirm = (String)emailVerificationInfo.get("confirm");

		if(confirm == null || !confirm.equals("Y")) {
			throw new MemberException(NOT_AUTHENTICATED_EMAIL);
		}

		return true;
	}

	/**
	 * 이메일 인증 코드 요청
	 */
	public void emailAuth(EmailDto emailDto) {
		String email = emailDto.getEmail();
		String authCode = generateCode();
		sendEmailVerification(email, authCode);
	}

	/**
	 * 이메일 인증 코드 확인 및 인증 상태 변경(Confirm 값 변경)
	 */
	public void emailAuthConfirm(EmailAuthDto emailAuthDto) {

		Map redisEmailInfo = redisRepository.getEmailCodeAndConfirm(emailAuthDto.getEmail());

		Optional.ofNullable(redisEmailInfo.get("authCode"))
			.ifPresentOrElse(
				authCode -> {
					if(emailAuthDto.getAuthCode().equals(authCode)) {
						redisRepository.setEmailCodeAndConfirm(emailAuthDto.getEmail(), (String)authCode, "Y");
					}else {
						throw new MemberException(NOT_MATCHED_AUTH_CODE);
					}
				},
				() -> {
					throw new MemberException(NOT_MATCHED_AUTH_CODE);
				}
			);
	}

	/**
	 * 회원 조회
	 */
	@Transactional(readOnly = true)
	public List<MemberDto> getAllMember() {
		return memberRepository.getAllMember();
	}

	/**
	 * 토큰 재발급
	 */
	public MemberTokenDto getNewAccessToken(UserDetailsImpl userDetail, String headerRefreshToken) {
		// 정상적으로 로그인 했는지를 판별하기 위해서 redis에 있는 refreshToken과 비교를 한다.
		Map redisRefreshToken = redisRepository.getRefreshToken(userDetail.getId());

		headerRefreshToken = headerRefreshToken.substring("Bearer ".length());

		if(headerRefreshToken.equals(String.valueOf(redisRefreshToken.get("refreshToken")))){
			MemberTokenDto tokenDto = createToken(userDetail);
			int refreshExpirationSeconds = jwtProvider.getRefreshExpirationSeconds();
			redisRepository.setRedisRefreshToken(userDetail.getId(), tokenDto.getRefreshToken(), refreshExpirationSeconds);
			return tokenDto;
		}else {
			throw new MemberException(VALIDATION_TOKEN_FAILED);
		}
	}

	/**
	 * email 중복 체크
	 */
	public void checkEmail(String email) {
		if(memberRepository.existsByEmail(email)){
			throw new MemberException(DUPLICATED_MEMBER_EMAIL);
		}
	}

	/**
	 * password, passwordConfirm 일치 여부 체크
	 */
	private void validatePassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(NOT_MATCHED_PASSWORD);
		}
	}

	/**
	 * phoneNumber 중복 체크
	 */
	public void checkPhoneNumber(String phoneNumber) {
		if(memberRepository.existsByPhoneNumber(phoneNumber)){
			throw new MemberException(DUPLICATED_MEMBER_PHONE_NUMBER);
		}
	}

	/**
	 * nickName 중복 체크
	 */
	public void checkNickName(String nickName) {
		if(memberRepository.existsByNickName(nickName)) {
			throw new MemberException(DUPLICATED_MEMBER_NICK_NAME);
		}
	}

	/**
	 * 마이 페이지 조회
	 */
	public MemberMyPageDto getMyPage(UserDetailsImpl userDetails) {
		return memberRepository.getMyPage(userDetails.getId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));
	}

	/**
	 * 이메일 인증 코드 생성
	 */
	private String generateCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 숫자 생성
		return String.valueOf(code);
	}

	/**
	 * 이메일 서버에 이메일 인증 코드 요청
	 */
	public void sendEmailVerification(String email, String authCode) {
		String url = serviceUrl + "/v1/mail/verification/send";

		try{
			Map<String, String> emailInfo = new HashMap<>();
			emailInfo.put("email", email);
			emailInfo.put("authCode", authCode);

			ResponseEntity<String> response = restTemplate.postForEntity(url, emailInfo, String.class);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new MemberException(UNABLE_TO_SEND_EMAIL);
			}

			redisRepository.setEmailCodeAndConfirm(email, authCode, "N");
		}catch (RestClientException e) {
			throw new MemberException(UNABLE_TO_SEND_EMAIL);
		}
	}

	/**
	 * 토큰 생성(Access Token, Refresh Token)
	 */
	private MemberTokenDto createToken(UserDetailsImpl userDetail) {
		String accessToken = jwtProvider.createAccessToken(userDetail);
		String refreshToken = jwtProvider.createRefreshToken(userDetail);

		return MemberTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
