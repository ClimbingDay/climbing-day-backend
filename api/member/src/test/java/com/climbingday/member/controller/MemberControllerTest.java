package com.climbingday.member.controller;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.infra.config.TestConfig;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
class MemberControllerTest extends TestConfig {

	private RequestSpecification spec;

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
	@DisplayName("1. 회원가입 테스트")
	@Transactional
	public void memberRegisterTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@naver.com")
			.name("test")
			.password("123456")
			.passwordConfirm("123456")
			.phoneNumber("010-1234-5678")
			.birthDate("2000-11-11")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("회원 가입 API",
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("name").type(STRING).description("이름"),
					fieldWithPath("password").type(STRING).description("패스워드"),
					fieldWithPath("passwordConfirm").type(STRING).description("패스워드 확인"),
					fieldWithPath("phoneNumber").type(STRING).description("핸드폰 번호"),
					fieldWithPath("birthDate").type(STRING).description("생년월일")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("아이디 고유번호")
				)))
			.contentType(JSON)
			.body(registerDto)
		.when()
			.post("/v1/member/register")
		.then().log().all()
			.statusCode(201);
	}

	@Test
	@DisplayName("2. 로그인 테스트")
	public void memberLoginTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("test@naver.com")
			.password("123456")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("로그인 API",
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("password").type(STRING).description("패스워드")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					fieldWithPath("data.accessToken").type(STRING).description("액세스 토큰"),
					fieldWithPath("data.refreshToken").type(STRING).description("리프래쉬 토큰")
				)))
			.contentType(JSON)
			.body(memberLoginDto)
		.when()
			.post("/v1/member/login")
		.then().log().all()
			.statusCode(200);
	}
}