package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@PostMapping("/post/register")
	public ResponseEntity<CDResponse<?>> registerPost(
		@Valid @RequestBody GeneralPostRegDto generalPostRegDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", generalService.registerPost(generalPostRegDto, userDetails))));
	}

	/**
	 * 모든 일반 게시글 조회
	 */
	@GetMapping("/post")
	public ResponseEntity<CDResponse<?>> getGeneralPosts(
		@RequestParam("page") int page,
		@RequestParam(name = "size", defaultValue = "10") int size
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(
				generalService.getGeneralPosts(
					PageRequest.of(page, size)
				)
			));
	}

	/**
	 * 일반 게시글 상세 조회
	 */
	@GetMapping("/post/{postId}")
	public ResponseEntity<CDResponse<?>> getGeneralDetailPost(
		@PathVariable Long postId
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(generalService.getGeneralDetailPost(postId)));
	}
}
