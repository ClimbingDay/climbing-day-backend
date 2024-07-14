package com.climbingday.event.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.member.MemberLocationDto;
import com.climbingday.event.service.EventService;
import com.climbingday.response.CDResponse;
import com.climbingday.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
	private final EventService eventService;

	@PostMapping("/member-match")
	public ResponseEntity<CDResponse<?>> memberMatch(
		@AuthenticationPrincipal UserDetailsImpl userDetail,
		@Valid  @RequestBody MemberLocationDto memberLocationDto
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(eventService.memberMatch(memberLocationDto, userDetail)));
	}
}
