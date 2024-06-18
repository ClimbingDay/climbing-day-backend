package com.climbingday.security.config;

import static com.climbingday.enums.member.ERoles.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.climbingday.security.exception.CustomAccessDeniedHandler;
import com.climbingday.security.jwt.JwtFilter;
import com.climbingday.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
	private final JwtProvider jwtProvider;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	/**
	 * 패스워드 인코더
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * 로그인 인증 할때 사용함
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * 사전 요청(Preflight Request) 확인 허용
	 * 서버의 허용 메서드 검사하는 기능 및 CORS 정책을 확인하는 용도
	 */
	@Bean
	public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
		httpSecuritySetting(http);
		http
			.securityMatchers(matcher -> matcher
				.requestMatchers(OPTIONS, "/**")
			);

		return http.build();
	}

	/**
	 * public http
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
		httpSecuritySetting(http);
		http
			.securityMatchers(matcher -> matcher
				.requestMatchers(permitAllRequestMatchers()))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(permitAllRequestMatchers()).permitAll()
				.anyRequest().authenticated()
			);
		return http.build();
	}

	/**
	 * token and auth http
	 */
	@Bean
	@Order(2)
	public SecurityFilterChain authenticatedFilterChain(HttpSecurity http) throws Exception {
		httpSecuritySetting(http);
		http
			.securityMatchers(matcher -> matcher
				.requestMatchers(AuthRequestMatchers()))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(AuthRequestMatchers()).hasAnyAuthority(
					ROLE_ADMIN.name(),
					ROLE_USER.name()
				)
				.anyRequest().authenticated()
			)
			.exceptionHandling(exception -> exception
				.accessDeniedHandler(customAccessDeniedHandler)
			)
			.addFilterBefore(new JwtFilter(jwtProvider), ExceptionTranslationFilter.class);
		return http.build();
	}

	/**
	 * 설정하지 않은 http 거부
	 */
	@Bean
	@Order(2)
	public SecurityFilterChain otherFilterChain(HttpSecurity http) throws Exception {
		httpSecuritySetting(http);
		http
			.securityMatchers(matcher -> matcher
				.requestMatchers("/**")
			)
			.authorizeHttpRequests(auth -> auth
				.anyRequest().denyAll()
			);

		return http.build();
	}

	/**
	 * permitAll endpoint
	 */
	private RequestMatcher[] permitAllRequestMatchers() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher(POST, "/member/login"),						// 로그인
			antMatcher(POST, "/member/register"),						// 회원가입
			antMatcher(GET, "/member/email/check"),					// 이메일 중복확인
			antMatcher(GET, "/member/phone-num/check"),				// 휴대폰 번호 중복확인
			antMatcher(GET, "/member/nick-name/check"),				// 닉네임 중복확인

			antMatcher(GET, "/center"),								// 암장 조회

			antMatcher(POST, "/member/email/auth/request"),			// 이메일 인증 코드 요청
			antMatcher(POST, "/member/email/auth/confirm"),			// 이메일 인증 코드 확인
			antMatcher(POST, "/mail/verification/send"),				// 이메일 메일 보내기

			// 스웨거
			antMatcher(GET, "/swagger-ui.html"),
			antMatcher(GET, "/swagger-ui/**"),
			antMatcher(GET, "/urls.json"),
			antMatcher(GET, "/openapi3.yaml")
		);

		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * Auth endpoint
	 */
	private RequestMatcher[] AuthRequestMatchers() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher(POST, "/center/register"),					// 암장 등록
			antMatcher(GET, "/admin/member"),						// 모든 회원 조회
			antMatcher(GET, "/member/token/refresh")				// AccessToken, RefreshToken 재발급
		);

		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * cors 설정
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		// List<String> allowedOrigins = Arrays.asList(
		// 	"http://localhost:8080",
		// 	"http://localhost:3000"
		// );
		//
		// CorsConfiguration configuration = new CorsConfiguration();
		// configuration.setAllowCredentials(true);															// 쿠키 및 인증 정보 전송
		// configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));			// 허용할 HTTP 메서드
		// configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));	// 허용할 헤더
		//
		// UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// // 실서버 반영할때 수정해야 됌
		// source.registerCorsConfiguration("/**", new CorsConfiguration() {
		// 	@Override
		// 	public String checkOrigin(String requestOrigin) {
		// 		if (requestOrigin != null && isAllowedOrigin(requestOrigin)) {
		// 			// 동적으로 특정 Origin 허용
		// 			return requestOrigin;
		// 		}
		// 		// null을 반환하면 CORS 요청이 허용되지 않음
		// 		return null;
		// 	}
		//
		// 	private boolean isAllowedOrigin(String requestOrigin) {
		// 		// 허용된 도메인 목록 확인 로직
		// 		return allowedOrigins.contains(requestOrigin);
		// 	}
		// });
		//
		// return source;

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true); // 크레덴셜 허용 설정
		configuration.addAllowedOriginPattern("*"); // 모든 출처 허용
		configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
		configuration.addAllowedHeader("*"); // 모든 헤더 허용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

		return source;
	}

	private void httpSecuritySetting(HttpSecurity http) throws Exception {
		http
			// jwt, OAuth 토큰을 사용 -> OAuth의 경우는 이슈가 발생할 수 있음 OAuth 구성할때 체크
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors 정책
			.formLogin(AbstractHttpConfigurer::disable) // form 기반 로그인을 사용하지 않음.
			.httpBasic(AbstractHttpConfigurer::disable) // 기본으로 제공하는 http 사용하지 않음
			.rememberMe(AbstractHttpConfigurer::disable) // 토큰 기반이므로 세션 기반의 인증 사용하지 않음
			.headers(headers -> headers.frameOptions(
				HeadersConfigurer.FrameOptionsConfig::disable)) // x-Frame-Options 헤더 비활성화, 클릭재킹 공격 관련
			.logout(AbstractHttpConfigurer::disable) // stateful 하지 않기때문에 필요하지 않음
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성을 하지 않음
			)
			.anonymous(AbstractHttpConfigurer::disable); // 익명 사용자 접근 제한, 모든 요청이 인증 필요
	}
}
