package com.climbingday.event.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.record.RecordRegisterDto;
import com.climbingday.event.service.RecordService;
import com.climbingday.response.CDResponse;
import com.climbingday.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {
	private final RecordService recordService;

	/**
	 * 기록 등록
	 */
	@PostMapping("/register")
	public ResponseEntity<CDResponse<?>> registerRecord(
		@Valid @RequestBody RecordRegisterDto recordRegisterDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", recordService.registerRecord(userDetails, recordRegisterDto))));
	}
}
