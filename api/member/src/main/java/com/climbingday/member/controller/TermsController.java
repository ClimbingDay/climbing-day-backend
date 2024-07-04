package com.climbingday.member.controller;

import static com.climbingday.enums.GlobalSuccessCode.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.member.service.TermsService;
import com.climbingday.response.CDResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController {

	private final ResourceLoader resourceLoader;
	private final TermsService termsService;

	/**
	 * 이용약관 내용(웹뷰)
	 */
	@GetMapping("/{contentUrl}")
	public ResponseEntity<Resource> getHtmlTerms(
		@PathVariable String contentUrl
	) {
		try {
			Resource resource = resourceLoader.getResource("classpath:static/" + contentUrl + ".html");

			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + contentUrl + ".html")
				.contentType(MediaType.TEXT_HTML)
				.body(resource);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 이용약관 목록 조회
	 */
	@GetMapping
	public ResponseEntity<CDResponse<?>> getTemrsList() {
		return ResponseEntity.status(SUCCESS.getStatus())
			.body(new CDResponse<>(termsService.getTermsList()));
	}
}
