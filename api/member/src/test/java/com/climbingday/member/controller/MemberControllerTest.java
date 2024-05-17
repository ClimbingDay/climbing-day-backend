package com.climbingday.member.controller;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.climbingday.infra.config.TestConfig;
import com.climbingday.member.dto.request.MemberRegisterDto;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class MemberControllerTest extends TestConfig {

	// 테스트 시 랜덤으로 설정된 port 를 가져옴
	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("일반 회원가입 테스트")
	public void memberRegisterTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@naver.com")
			.name("test")
			.password("123456")
			.passwordConfirm("123456")
			.phoneNumber(List.of("010", "1234", "5678"))
			.build();

		given()
			.contentType(JSON)
			.body(registerDto)
		.when()
			.post("/v1/member/register")
		.then()
			.statusCode(201)
			.body("code", equalTo(201))
			.body("message", equalTo("정상적으로 생성되었습니다."))
			.body("data.id", equalTo(1));
	}
}