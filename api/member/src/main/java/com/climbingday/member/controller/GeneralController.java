package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.general.GeneralPostRegDto;
import com.climbingday.member.service.GeneralService;
import com.climbingday.response.CDResponse;
import com.climbingday.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/general")
public class GeneralController {

	private final GeneralService generalService;

	/**
	 * 일반 게시글 등록
	 */
	@PostMapping("/post")
	public ResponseEntity<CDResponse<?>> registerPost(
		@Valid @RequestBody GeneralPostRegDto generalPostRegDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", generalService.registerPost(generalPostRegDto, userDetails))));
	}
}
