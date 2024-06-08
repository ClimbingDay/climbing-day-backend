package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.member.EmailAuthDto;
import com.climbingday.dto.member.EmailDto;
import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.member.service.MemberService;
import com.climbingday.response.CDResponse;
import com.climbingday.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	private final MemberService memberService;

	/**
	 * 회원 가입
	 */
	@PostMapping("/register")
	public ResponseEntity<CDResponse<?>> registerMember(
		@Valid @RequestBody MemberRegisterDto reqMemberDto) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", memberService.registerMember(reqMemberDto))));
	}

	/**
	 * 로그인
	 */
	@PostMapping("/login")
	public ResponseEntity<CDResponse<?>> login(
		@Valid @RequestBody MemberLoginDto memberLoginDto) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(memberService.login(memberLoginDto)));
	}

	/**
	 * 이메일 인증 코드 요청
	 */
	@PostMapping("/email/auth/request")
	public ResponseEntity<CDResponse<?>> emailAuthRequest(
		@RequestBody @Valid EmailDto emailDto) {
		memberService.emailAuth(emailDto);

		return ResponseEntity.status(EMAIL_SEND_SUCCESS.getStatus())
			.body(new CDResponse<>(EMAIL_SEND_SUCCESS));
	}

	/**
	 * 이메일 인증 코드 확인
	 */
	@PostMapping("/email/auth/confirm")
	public ResponseEntity<CDResponse<?>> emailAuthConfirm(
		@RequestBody @Valid EmailAuthDto emailAuthDto) {
		memberService.emailAuthConfirm(emailAuthDto);
		return ResponseEntity.status(EMAIL_AUTH_CONFIRM.getStatus())
			.body(new CDResponse<>(EMAIL_AUTH_CONFIRM));
	}

	/**
	 * Refresh Token 사용한 Access Token, Refresh Token 재발급
	 */
	@GetMapping("/token/refresh")
	public ResponseEntity<CDResponse<?>> refreshToken(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestHeader("Refresh-Token") String headerRefreshToken
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(memberService.getNewAccessToken(userDetails, headerRefreshToken)));
	}
}
