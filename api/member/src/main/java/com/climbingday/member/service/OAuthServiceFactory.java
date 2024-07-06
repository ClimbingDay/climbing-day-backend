package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.climbingday.enums.member.EProviders;
import com.climbingday.member.exception.MemberException;

@Service
public class OAuthServiceFactory {
	private final Map<String, OAuthService> serviceMap;

	@Autowired
	public OAuthServiceFactory(List<OAuthService> services) {
		// 서비스 리스트를 맵으로 변환하여 저장
		serviceMap = services.stream().collect(Collectors.toMap(
			service -> service.getClass().getSimpleName().replace("OAuthService", "").toUpperCase(),
			service -> service
		));
	}

	public OAuthService getService(String provider) {
		OAuthService service;
		// 지원하는 소셜로그인인지 확인
		try {
			EProviders.valueOf(provider.toUpperCase());
			service = serviceMap.get(provider.toUpperCase());

			if (service == null) {
				throw new IllegalArgumentException("Unsupported provider: " + provider);
			}
		}catch(IllegalArgumentException e) {
			throw new MemberException(UNSUPPORTED_OAUTH_LOGIN);
		}

		return service;
	}
}
