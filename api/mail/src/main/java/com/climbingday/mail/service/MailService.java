package com.climbingday.mail.service;

import static com.climbingday.enums.MailErrorCode.*;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.climbingday.dto.mail.EmailVerificationDto;
import com.climbingday.mail.exception.MailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;

	public void sendEmail(EmailVerificationDto emailVerificationDto) {
		String email = emailVerificationDto.getEmail();
		String title = emailVerificationDto.getTitle();
		String authCode = emailVerificationDto.getAuthCode();

		SimpleMailMessage emailForm = createEmailForm(email, title, authCode);

		try{
			mailSender.send(emailForm);
		}catch(RuntimeException e) {
			log.debug("MailService.sendEmail exception occur toEmail: {}, " +
				"title: {}, authCode: {}", email, title, authCode);
			throw new MailException(UNABLE_TO_SEND_EMAIL);
		}
	}

	private SimpleMailMessage createEmailForm(String toEmail, String title, String authCode) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(authCode);

		return message;
	}
}
