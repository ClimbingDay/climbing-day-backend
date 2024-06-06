package com.climbingday.mail.service;

import static com.climbingday.enums.MailErrorCode.*;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.climbingday.dto.mail.EmailVerificationDto;
import com.climbingday.mail.exception.MailException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;

	/**
	 * 이메일 전송
	 */
	public void sendEmail(EmailVerificationDto emailVerificationDto) {
		MimeMessage message = createEmailForm(emailVerificationDto);

		mailSender.send(message);
	}

	/**
	 * 인증 이메일 html 메시지 생성
	 */
	private MimeMessage createEmailForm(EmailVerificationDto emailVerificationDto) {
		String email = emailVerificationDto.getEmail();
		String title = emailVerificationDto.getTitle();
		String authCode = emailVerificationDto.getAuthCode();

		try{
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(email);
			helper.setSubject(title);

			String htmlContent = "<h1>클라이밍 데이</h1>"
				+ "<p>이메일 인증 코드: <strong>" + authCode + "</strong></p>";
			helper.setText(htmlContent, true);

			return message;
		}catch(MessagingException e) {
			log.debug("MailService.sendEmail exception occur toEmail: {}, " +
				"title: {}, authCode: {}", email, title, authCode);
			throw new MailException(UNABLE_TO_SEND_EMAIL);
		}
	}
}
