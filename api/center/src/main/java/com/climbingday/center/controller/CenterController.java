package com.climbingday.center.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.center.service.CenterService;
import com.climbingday.enums.GlobalSuccessCode;
import com.climbingday.response.CDResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/center")
public class CenterController {
	private final CenterService centerService;

	@GetMapping
	public ResponseEntity<CDResponse<?>> getCenter(
		@PageableDefault(size = 10) Pageable pageable
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(centerService.getCenterPage(pageable)));
	}
}
