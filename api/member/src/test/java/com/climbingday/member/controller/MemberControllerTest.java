package com.climbingday.member.controller;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;

import java.util.List;

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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.infra.config.TestConfig;
import com.climbingday.member.dto.MemberRegisterDto;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
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
	@DisplayName("1. 회원가입")
	@Transactional
	public void memberRegisterTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@naver.com")
			.name("test")
			.password("123456")
			.passwordConfirm("123456")
			.phoneNumber(List.of("010", "1234", "5678"))
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("회원 가입 API",
				requestFields(
					fieldWithPath("email").type(STRING).description("The user's email address."),
					fieldWithPath("name").type(STRING).description("The user's name."),
					fieldWithPath("password").type(STRING).description("The user's password."),
					fieldWithPath("passwordConfirm").type(STRING).description("Confirmation of the user's password."),
					fieldWithPath("phoneNumber").type(ARRAY).description("The user's phone number.")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("Response status code."),
					fieldWithPath("message").type(STRING).description("Response status message."),
					fieldWithPath("data.id").type(NUMBER).description("The ID of the created user.")
				)))
			.contentType(JSON)
			.body(registerDto)
		.when()
			.post("/v1/member/register")
		.then().log().all()
			.statusCode(201);
	}
}