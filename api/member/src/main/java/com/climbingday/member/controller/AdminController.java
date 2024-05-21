package com.climbingday.member.controller;

import static com.climbingday.domain.common.enums.GlobalSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.domain.response.CDResponse;
import com.climbingday.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final MemberService memberService;

	@GetMapping("/member")
	public ResponseEntity<CDResponse<?>> getAllMember() {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(CREATE, memberService.getAllMember()));
	}

	// @PostMapping("/auth")
	// public ResponseEntity<CDResponse<?>> changeMemberRoles(
	// 	@AuthenticationPrincipal UserDetailsImpl userDetails
	// ) {
	//
	// })
}
