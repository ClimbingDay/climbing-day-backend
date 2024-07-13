package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.aop.framework.AopProxyUtils;
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
		// CGLIB를 사용하여 프록시 클래스를 생성하고, 이 경우 클래스 이름이 예상과 다를 수 있음
		// AopProxyUtils.ultimateTargetClass를 사용하면 프록시의 실제 타겟 클래스를 반환
		serviceMap = services.stream().collect(Collectors.toMap(
			service -> AopProxyUtils.ultimateTargetClass(service).getSimpleName().replace("OAuthService", "").toUpperCase(),
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
