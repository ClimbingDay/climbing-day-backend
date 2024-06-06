package com.climbingday.domain.common.repository;

import static com.climbingday.enums.RedisErrorCode.*;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.climbingday.domain.exception.CustomRedisException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

	@Value("${spring.mail.auth-code-expiration-millis}")
	private int authCodeSeconds;

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * SET 이메일 인증코드
	 */
	public void setEmailCodeAndConfirm(String email, String authCode, boolean confirm) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(email,
			Map.of(
				"authCode", authCode,
				"confirm", confirm
			), createExpireDuration(authCodeSeconds));
	}

	/**
	 * GET 이메일 인증코드
	 */
	public Map getEmailCode(String email) {
		return getKeyIfPresent(email);
	}

	/**
	 * SET refresh token
	 */
	public void setRedisRefreshToken(Long memberId, String refreshToken, int expirationSeconds) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(String.valueOf(memberId), Map.of("refreshToken", refreshToken), createExpireDuration(expirationSeconds));
	}

	/**
	 * GET refresh token
	 */
	public Object getRefreshToken(Long memberId) {
		return redisTemplate
			.opsForValue()
			.get(String.valueOf(memberId));
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
	private Map getKeyIfPresent(String key) {
		Optional<Object> optionalResult = Optional.ofNullable(redisTemplate.opsForValue().get(key));

		if(optionalResult.isPresent()) {
			return (Map)optionalResult.get();
		}else {
			throw new CustomRedisException(REDIS_EMPTY_KEY);
		}
	}
}
