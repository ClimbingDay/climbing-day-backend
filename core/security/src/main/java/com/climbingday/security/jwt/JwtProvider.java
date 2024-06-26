package com.climbingday.security.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.climbingday.security.service.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final String AUTHENTICATION_CLAIM_NAME = "roles";

	@Value("${jwt.access-expiration-seconds}")
	private int accessExpirationSeconds;	// 엑세스 토큰 만료 시간

	/**
	 *  getter 리프래쉬 토큰 만료시간
	 */
	@Getter
	@Value("${jwt.refresh-expiration-seconds}")
	private int refreshExpirationSeconds;	// 리프래쉬 토큰 만료 시간

	@Value("${jwt.access-key}")
	private String accessSecret;				// 엑세스 키

	/**
	 * 엑세스 토큰 생성
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
			.claim("nickName", userDetails.getNickName())
			.setSubject(userDetails.getUsername())
			.setIssuedAt(Date.from(now))
			.setExpiration(expiration)
			.claim(AUTHENTICATION_CLAIM_NAME, roles.toString())
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * 리프래쉬 토큰 생성
	 */
	public String createRefreshToken(UserDetailsImpl userDetails) {
		Instant now = Instant.now();
		Date expiration = Date.from(now.plusSeconds(refreshExpirationSeconds));
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
			.claim("nickName", userDetails.getNickName())
			.setSubject(userDetails.getUsername())
			.setIssuedAt(Date.from(now))
			.setExpiration(expiration)
			.claim(AUTHENTICATION_CLAIM_NAME, roles.toString())
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * 권한 체크
	 */
	public Authentication toAuthentication(String token) {
		JwtParser jwtParser = Jwts.parserBuilder()
			.setSigningKey(extractSecretKey())
			.build();
		Claims claims = jwtParser.parseClaimsJws(token).getBody();

		Object roles = claims.get(AUTHENTICATION_CLAIM_NAME);
		List<GrantedAuthority> authorities = null;
		if(roles != null && !roles.toString().trim().isEmpty()) {
			authorities = List.of(new SimpleGrantedAuthority(roles.toString()));
		}

		UserDetails user = UserDetailsImpl.builder()
			.id(claims.get("id", Long.class))
			.email(claims.getSubject())
			.password(null)
			.nickName(claims.get("nickName", String.class))
			.authorities(authorities)
			.build();

		return new UsernamePasswordAuthenticationToken(user, token, authorities);
	}

	/**
	 * 토큰 검증
	 */
	public void validate(String token) {
		JwtParser jwtParser = Jwts.parserBuilder()
			.setSigningKey(extractSecretKey())
			.build();

		jwtParser.parseClaimsJws(token);
	}

	/**
	 * SecretKey 추출
	 */
	private SecretKey extractSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
	}
}
