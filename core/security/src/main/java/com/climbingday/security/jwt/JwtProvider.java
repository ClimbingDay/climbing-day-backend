package com.climbingday.security.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.climbingday.security.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final String AUTHENTICATION_CLAIM_NAME = "roles";

	@Value("${jwt.access-expiration-seconds}")
	private int accessExpirationSeconds;	// 엑세스 토큰 만료 시간

	@Value("${jwt.access-key}")
	private String accessSecret;				// 엑세스 키

	/**
	 * 토큰 생성
	 */
	public String createAccessToken(UserDetailsImpl userDetails) {
		Instant now = Instant.now();
		Date expiration = Date.from(now.plusSeconds(accessExpirationSeconds));
		SecretKey key = extractSecretKey();

		StringBuilder roles = new StringBuilder();
		// member roles 추출
		if(userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
			roles.append(
				userDetails.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(", "))
			);
		}

		return Jwts.builder()
			.claim("id", userDetails.getId())
			.setSubject(userDetails.getUsername())
			.setIssuedAt(Date.from(now))
			.setExpiration(expiration)
			.claim(AUTHENTICATION_CLAIM_NAME, roles.toString())
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * SecretKey 추출
	 */
	private SecretKey extractSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
	}
}
