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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import com.climbingday.infra.config.TestConfig;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
public class MemberDuplicateTest extends TestConfig {
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
	@DisplayName("1-1. 이메일 중복체크 - 성공")
	public void memberEmailCheck1Test() throws Exception {
		given(spec).log().all()
			.filter(document("이메일 중복체크 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 중복체크"),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지")
				)))
			.queryParams("email", "emailCheck@gmail.com")
			.contentType(JSON)
		.when()
			.get("/v1/member/email/check")
		.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("1-2. 이메일 중복체크 - 실패: 이미 존재하는 이메일")
	public void memberEmailCheck2Test() throws Exception {
		given(spec).log().all()
			.filter(document("이메일 중복체크 API - 실패: 이미 존재하는 이메일",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 중복체크"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.queryParam("email", "admin@gmail.com")
			.contentType(JSON)
			.when()
			.get("/v1/member/email/check")
			.then().log().all()
			.statusCode(409);
	}

	@Test
	@DisplayName("2-1. 핸드폰 번호 중복체크 - 성공")
	public void memberPhoneNumCheck1Test() throws Exception {
		given(spec).log().all()
			.filter(document("핸드폰 번호 중복체크 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("핸드폰 번호 중복체크"),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지")
				)))
			.queryParam("phoneNum", "010-122-1111")
			.contentType(JSON)
			.when()
			.get("/v1/member/phone-num/check")
			.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("2-2. 핸드폰 번호 중복체크 - 실패: 이미 존재하는 휴대폰 번호")
	public void memberPhoneNumCheck2Test() throws Exception {
		given(spec).log().all()
			.filter(document("핸드폰 번호 중복체크 API - 실패: 이미 존재하는 휴대폰 번호",
				resourceDetails()
					.tag("회원 API")
					.summary("핸드폰 번호 중복체크"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.queryParam("phoneNum", "010-000-0000")
			.contentType(JSON)
			.when()
			.get("/v1/member/phone-num/check")
			.then().log().all()
			.statusCode(409);
	}

	@Test
	@DisplayName("3-1. 닉네임 중복체크 - 성공")
	public void memberNickNameCheck1Test() throws Exception {
		given(spec).log().all()
			.filter(document("닉네임 중복체크 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("닉네임 중복체크"),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지")
				)))
			.queryParam("nickName", "nickName")
			.contentType(JSON)
		.when()
			.get("/v1/member/nick-name/check")
		.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("3-2. 닉네임 중복체크 - 실패: 이미 존재하는 닉네임")
	public void memberNickNameCheck2Test() throws Exception {
		given(spec).log().all()
			.filter(document("닉네임 중복체크 API - 실패: 이미 존재하는 닉네임",
				resourceDetails()
					.tag("회원 API")
					.summary("닉네임 중복체크"),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.queryParam("nickName", "admin")
			.contentType(JSON)
		.when()
			.get("/v1/member/nick-name/check")
		.then().log().all()
			.statusCode(409);
	}
}

