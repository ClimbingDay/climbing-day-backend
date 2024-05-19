package com.climbingday.member.controller;

import static com.climbingday.domain.common.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.domain.member.repository.CDResponse;
import com.climbingday.member.dto.MemberLoginDto;
import com.climbingday.member.dto.MemberRegisterDto;
import com.climbingday.member.service.MemberService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name ="회원", description = "회원 API")
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
}
