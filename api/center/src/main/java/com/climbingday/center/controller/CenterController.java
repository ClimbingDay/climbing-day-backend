package com.climbingday.center.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.climbingday.center.service.CenterService;
import com.climbingday.dto.center.CenterRegisterDto;
import com.climbingday.response.CDResponse;
import com.climbingday.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/center")
public class CenterController {
	private final CenterService centerService;

	/**
	 * 암장 등록
	 */
	@PostMapping("/register")
	public ResponseEntity<CDResponse<?>> registerCenter(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestPart("center") CenterRegisterDto centerRegisterDto,
		@RequestParam(value = "profile_image", required = false)MultipartFile file
		) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", centerService.registerCenter(centerRegisterDto, file, userDetails))));
	}

	/**
	 * 암장 조회
	 */
	@GetMapping
	public ResponseEntity<CDResponse<?>> getCenter(
		@RequestParam("page") int page
	) {
		int defaultSize = 10; // 기본 페이지 크기
		Pageable defaultPageable = PageRequest.of(page, defaultSize);
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(centerService.getCenterPage(defaultPageable)));
	}
}
