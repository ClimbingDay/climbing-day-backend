package com.climbingday.member.controller;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import com.climbingday.domain.common.repository.RedisRepository;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.infra.config.TestConfig;
import com.climbingday.member.service.MemberService;

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
	private MemberService memberService;

	@SpyBean
	private RedisRepository redisRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	// 테스트 시 랜덤으로 설정된 port 를 가져옴
	@LocalServerPort
	private int port;

	@BeforeEach
	void setup(RestDocumentationContextProvider provider) {
		RestAssured.port = port;
		this.spec = new RequestSpecBuilder()
			.addFilter(documentationConfiguration(provider))
			.build();
	}

	@Test
	@DisplayName("1-1. 토큰 재발급 테스트 - 성공")
	public void refreshTokenTest() throws Exception {
		String redisRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3ODQzMDg0LCJleHAiOjE3MTc5MDMwODQsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.Q7ywSKRv3RJ6hyKbwOxUhfpS8AlrRILznP_6jSRm7NXn_TAjiIjwWTJdd49Mg7KoKHk7lpWpYI1wHzi9vi_fYg";
		String refreshToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE3ODQzMDg0LCJleHAiOjE3MTc5MDMwODQsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.Q7ywSKRv3RJ6hyKbwOxUhfpS8AlrRILznP_6jSRm7NXn_TAjiIjwWTJdd49Mg7KoKHk7lpWpYI1wHzi9vi_fYg";

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
	@DisplayName("1-2. 토큰 재발급 테스트 - 실패")
	public void refreshTokenFailTest() throws Exception {

	}
}
