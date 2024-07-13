package com.climbingday.event.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.event.service.CommonService;
import com.climbingday.response.CDResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {
	private final CommonService commonService;

	/**
	 * 시군구 조회
	 */
	@GetMapping("/sigungu")
	public ResponseEntity<CDResponse<?>> getSigunguList(
		@RequestParam String name
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(commonService.getSigunguList(name)));
	}
}
