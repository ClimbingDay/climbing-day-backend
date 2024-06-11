package com.climbingday.member.controller;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.common.repository.RedisRepository;
import com.climbingday.infra.config.TestConfig;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
public class MemberControllerTokenTest extends TestConfig {
	private RequestSpecification spec;

	@SpyBean
	private RedisRepository redisRepository;

	// 테스트 시 랜덤으로 설정된 port 를 가져옴
	@LocalServerPort
	private int port;

	private final String redisRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE4MTExMzk0LCJleHAiOjE4MTgxMTEzOTMsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.JitpZyXPoT9lol5axaROUX0i7Q8Fu_YoOKmRNwfjhDjSWI8VFa6iqueZKgWvz87oWUdIWLpp6ZoU0V3tPnhU6A";
	private final String refreshToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE4MTExMzk0LCJleHAiOjE4MTgxMTEzOTMsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.JitpZyXPoT9lol5axaROUX0i7Q8Fu_YoOKmRNwfjhDjSWI8VFa6iqueZKgWvz87oWUdIWLpp6ZoU0V3tPnhU6A";

	@BeforeEach
	void setup(RestDocumentationContextProvider provider) {
		RestAssured.port = port;
		this.spec = new RequestSpecBuilder()
			.addFilter(documentationConfiguration(provider))
			.build();
	}

	@Test
	@DisplayName("1-1. 토큰 재발급 테스트 - 성공")
	@Transactional
	public void refreshTokenTest() throws Exception {
		redisRepository.setRedisRefreshToken(1L, redisRefreshToken, 1111111);

		given(spec).log().all()
			.filter(document("토큰 재발급 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("토큰 재발급"),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					fieldWithPath("data.accessToken").type(STRING).description("액세스 토큰"),
					fieldWithPath("data.refreshToken").type(STRING).description("리프래쉬 토큰")
				)))
			.header("Authorization", refreshToken)
			.contentType(JSON)
		.when()
			.get("/v1/member/token/refresh")
		.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("1-2. 토큰 재발급 테스트 - 실패: 리프래시 토큰 불일치")
	@Transactional
	public void refreshTokenFail1Test() throws Exception {
		redisRepository.setRedisRefreshToken(1L, redisRefreshToken + "error", 1111111);

		given(spec).log().all()
			.filter(document("토큰 재발급 API - 실패: 리프래시 토큰 불일치",
				resourceDetails()
					.tag("회원 API")
					.summary("토큰 재발급"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.header("Authorization", refreshToken)
			.contentType(JSON)
		.when()
			.get("/v1/member/token/refresh")
		.then().log().all()
			.statusCode(401);
	}

	@Test
	@DisplayName("1-3. 토큰 재발급 테스트 - 실패: 토큰 없음")
	@Transactional
	public void refreshTokenFail2Test() throws Exception {
		redisRepository.setRedisRefreshToken(1L, redisRefreshToken, 1111111);

		given(spec).log().all()
			.filter(document("토큰 재발급 API - 실패: 토큰 없음",
				resourceDetails()
					.tag("회원 API")
					.summary("토큰 재발급"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
		.when()
			.get("/v1/member/token/refresh")
		.then().log().all()
			.statusCode(401);
	}

	@Test
	@DisplayName("1-4. 토큰 재발급 테스트 - 실패: 토큰 유효기한 만료")
	@Transactional
	public void refreshTokenFail3Test() throws Exception {
		String redisRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3ODQzMDg0LCJleHAiOjE3MTc5MDMwODQsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.Q7ywSKRv3RJ6hyKbwOxUhfpS8AlrRILznP_6jSRm7NXn_TAjiIjwWTJdd49Mg7KoKHk7lpWpYI1wHzi9vi_fYg";
		String refreshToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3ODQzMDg0LCJleHAiOjE3MTc5MDMwODQsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.Q7ywSKRv3RJ6hyKbwOxUhfpS8AlrRILznP_6jSRm7NXn_TAjiIjwWTJdd49Mg7KoKHk7lpWpYI1wHzi9vi_fYg";
		redisRepository.setRedisRefreshToken(1L, redisRefreshToken, 1111111);

		given(spec).log().all()
			.filter(document("토큰 재발급 API - 실패: 토큰 유효기한 만료",
				resourceDetails()
					.tag("회원 API")
					.summary("토큰 재발급"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.header("Authorization", refreshToken)
			.contentType(JSON)
		.when()
			.get("/v1/member/token/refresh")
		.then().log().all()
			.statusCode(401);
	}


	@Test
	@DisplayName("1-5. 토큰 재발급 테스트 - 실패: 권한 없음")
	@Transactional
	public void refreshTokenFail4Test() throws Exception {
		String redisRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6Miwic3ViIjoiZ3Vlc3RAZ21haWwuY29tIiwiaWF0IjoxNzE4MTE0Njk3LCJleHAiOjE4MTgxMTQ2OTYsInJvbGVzIjoiUk9MRV9HVUVTVCJ9.gr68jTuoz_9VBbEbXFeBzzCOCFyMv4C3iOHI3w6bZoy9LXn50tqCbLESPka-HPtUs-J8GEo4P-3PwVohnKcitg";
		String refreshToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6Miwic3ViIjoiZ3Vlc3RAZ21haWwuY29tIiwiaWF0IjoxNzE4MTE0Njk3LCJleHAiOjE4MTgxMTQ2OTYsInJvbGVzIjoiUk9MRV9HVUVTVCJ9.gr68jTuoz_9VBbEbXFeBzzCOCFyMv4C3iOHI3w6bZoy9LXn50tqCbLESPka-HPtUs-J8GEo4P-3PwVohnKcitg";
		redisRepository.setRedisRefreshToken(2L, redisRefreshToken, 1111111);

		given(spec).log().all()
			.filter(document("토큰 재발급 API - 실패: 권한 없음",
				resourceDetails()
					.tag("회원 API")
					.summary("토큰 재발급"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.header("Authorization", refreshToken)
			.contentType(JSON)
			.when()
			.get("/v1/member/token/refresh")
			.then().log().all()
			.statusCode(403);
	}
}
