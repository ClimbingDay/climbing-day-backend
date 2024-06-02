package com.climbingday.mail.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingday.dto.mail.EmailVerificationDto;
import com.climbingday.mail.service.MailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class VerificationController {
	private final MailService mailService;

	@PostMapping("/verification/send")
	public ResponseEntity sendMessage(
		@RequestBody @Valid EmailVerificationDto emailVerificationDto) {
		mailService.sendEmail(emailVerificationDto);
		return new ResponseEntity<>(OK);
	}
}
