package com.climbingday.domain.common.repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * refresh token 저장
	 */
	public void setRedisRefreshToken(Long memberId, String refreshToken, int expirationSeconds) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		Duration expireDuration = Duration.ofSeconds(expirationSeconds);
		valueOperations.set(String.valueOf(memberId), Map.of("refreshToken", refreshToken), expireDuration);
	}

	/**
	 * refresh token 가져오기
	 */
	public Object getRefreshToken(Long memberId) {
		return redisTemplate
			.opsForValue()
			.get(String.valueOf(memberId));
	}
}
