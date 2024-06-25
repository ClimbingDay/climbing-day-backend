package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.crew.CrewPostRegDto;
import com.climbingday.member.service.CrewService;
import com.climbingday.response.CDResponse;
import com.climbingday.security.service.UserDetailsImpl;

import jakarta.validation.Valid;
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
		@RequestParam("page") int page,
		@RequestParam(name = "size", defaultValue = "10") int size
	) {
		Pageable defaultPageable = PageRequest.of(page, size);
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(crewService.getCrewProfilePage(defaultPageable)));
	}

	/**
	 * 나의 크루 프로필 조회(크루 번호, 이름, 프로필)
	 */
	@GetMapping("/profile/my")
	public ResponseEntity<CDResponse<?>> getMyCrewProfile(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(crewService.getMyCrewProfile(userDetails)));
	}

	/**
	 * 크루 게시글 등록
	 */
	@PostMapping("/post/register")
	public ResponseEntity<CDResponse<?>> registerPost(
		@Valid @RequestBody CrewPostRegDto crewPostRegDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return ResponseEntity.status(CREATE.getStatus())
			.body(new CDResponse<>(CREATE, Map.of("id", crewService.registerPost(crewPostRegDto, userDetails))));
	}

	/**
	 * 모든 크루 게시글 조회
	 */
	@GetMapping("/post")
	public ResponseEntity<CDResponse<?>> getCrewPosts(
		@RequestParam("page") int page,
		@RequestParam(name = "size", defaultValue = "10") int size
	) {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(
				crewService.getCrewPosts(
					PageRequest.of(page, size)
				)
			));
	}
}
