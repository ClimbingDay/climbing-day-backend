package com.climbingday.domain.common.repository;

import static com.climbingday.enums.RedisErrorCode.*;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.climbingday.domain.common.exception.CustomRedisException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

	@Value("${spring.mail.auth-code-expiration-millis}")
	private int authCodeSeconds;

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * SET Email AuthCode AND Confirm - 이메일 인증, 확인정보 저장
	 */
	public void setEmailCodeAndConfirm(String email, String authCode, String confirm) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(email,
			Map.of(
				"authCode", authCode,
				"confirm", confirm
			), createExpireDuration(authCodeSeconds));
	}

	/**
	 * GET Email AuthCode AND Confirm - 이메일 인증, 확인정보 가져오기
	 */
	public Map getEmailCodeAndConfirm(String email) {
		Optional<Object> optionalResult = getKeyIfPresent(email);
		if(optionalResult.isPresent()) {
			return (Map)optionalResult.get();
		}else {
			throw new CustomRedisException(NOT_EXIST_EMAIL_INFO);
		}
	}

	/**
	 * DELETE Redis Key
	 */
	public void deleteRedisInfo(String email) {
		redisTemplate.delete(email);
	}

	/**
	 * SET refresh token - 리프래쉬 토큰 정보 저장
	 */
	public void setRedisRefreshToken(Long memberId, String refreshToken, int expirationSeconds) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(String.valueOf(memberId), Map.of("refreshToken", refreshToken), createExpireDuration(expirationSeconds));
	}

	/**
	 * GET refresh token - 리프래쉬 토큰 정보 가져오기
	 */
	public Map getRefreshToken(Long memberId) {
		Optional<Object> optionalResult = getKeyIfPresent(String.valueOf(memberId));
		if(optionalResult.isPresent()) {
			return (Map)optionalResult.get();
		}else {
			throw new CustomRedisException(NOT_EXIST_REFRESH_TOKEN);
		}
	}

	/**
	 * 만료 시간 생성
	 */
	private Duration createExpireDuration(int seconds) {
		return Duration.ofSeconds(seconds);
	}

	/**
	 * redis key null 체크
	 */
	private Optional getKeyIfPresent(String key) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(key));
	}
}
