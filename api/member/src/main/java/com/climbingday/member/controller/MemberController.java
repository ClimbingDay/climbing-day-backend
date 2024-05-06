package com.climbingday.member.controller;

import static com.climbingday.domain.common.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.domain.member.repository.CResponse;
import com.climbingday.member.dto.request.MemberRegisterDto;
import com.climbingday.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<CResponse<?>> registerMember(
		@Valid @RequestBody MemberRegisterDto reqMemberDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new CResponse<>(CREATE, Map.of("id", memberService.registerMember(reqMemberDto))));
	}
}
