package com.climbingday.member.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController {

	@Autowired
	private ResourceLoader resourceLoader;

	@GetMapping("/test")
	public ResponseEntity<Resource> getHtmlTerms() {
		try {
			Resource resource = resourceLoader.getResource("classpath:static/terms.html");

			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=terms.html")
				.contentType(MediaType.TEXT_HTML)
				.body(resource);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
