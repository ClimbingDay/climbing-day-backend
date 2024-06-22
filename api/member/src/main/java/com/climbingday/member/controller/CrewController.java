package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.member.service.CrewService;
import com.climbingday.response.CDResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crew")
public class CrewController {

	private final CrewService crewService;

	/**
	 * 모든 크루 프로필 조회(크루 번호, 이름, 프로필)
	 */
	@GetMapping("/profile")
	public ResponseEntity<CDResponse<?>> getCrewProfile(
		@RequestParam("page") int page
	) {
		int defaultSize = 10;
		Pageable defaultPageable = PageRequest.of(page, defaultSize);
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(crewService.getCrewProfilePage(defaultPageable)));
	}
}
