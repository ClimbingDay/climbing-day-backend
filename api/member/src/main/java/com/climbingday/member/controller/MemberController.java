package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.member.EmailAuthDto;
import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.member.service.MemberService;
import com.climbingday.response.CDResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<CDResponse<?>> registerMember(
		@Valid @RequestBody MemberRegisterDto reqMemberDto) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", memberService.registerMember(reqMemberDto))));
	}

	@PostMapping("/login")
	public ResponseEntity<CDResponse<?>> login(
		@Valid @RequestBody MemberLoginDto memberLoginDto) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(memberService.login(memberLoginDto)));
	}

	@PostMapping("/email/auth/request")
	public ResponseEntity<CDResponse<?>> emailAuthRequest(
		@RequestBody @Valid EmailAuthDto emailAuthDto) {
		memberService.emailAuth(emailAuthDto);

		return ResponseEntity.status(EMAIL_SEND_SUCCESS.getStatus())
			.body(new CDResponse<>(EMAIL_SEND_SUCCESS));
	}
}
