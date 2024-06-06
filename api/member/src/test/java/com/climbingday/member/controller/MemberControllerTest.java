package com.climbingday.member.controller;

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

import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.infra.config.TestConfig;
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
	@DisplayName("1-1. 회원가입 테스트")
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
			.filter(RestAssuredRestDocumentationWrapper.document("회원 가입 API - 성공",
				RestAssuredRestDocumentationWrapper.resourceDetails()
					.tag("회원 API")
					.summary("회원 가입"),
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
	@DisplayName("1-2. 회원가입 중복 회원 실패 테스트")
	public void memberRegisterDuplicatedTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@naver.com")
			.name("test")
			.password("123456")
			.passwordConfirm("123456")
			.phoneNumber("010-1234-5678")
			.birthDate("2000-11-11")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("회원 가입 API - 실패: 중복 회원",
				RestAssuredRestDocumentationWrapper.resourceDetails()
					.tag("회원 API")
					.summary("회원 가입"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("name").type(STRING).description("이름"),
					fieldWithPath("password").type(STRING).description("패스워드"),
					fieldWithPath("passwordConfirm").type(STRING).description("패스워드 확인"),
					fieldWithPath("phoneNumber").type(STRING).description("핸드폰 번호"),
					fieldWithPath("birthDate").type(STRING).description("생년월일")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(registerDto)
			.when()
				.post("/v1/member/register")
			.then().log().all()
				.statusCode(409);
	}

	@Test
	@DisplayName("1-3. 회원가입 입력값 유효성 실패 테스트")
	public void memberRegisterValidTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("testnaver.com")
			.name("")
			.password("12345")
			.passwordConfirm("12345")
			.phoneNumber("0101234-5678")
			.birthDate("200011-11")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("회원 가입 API - 실패: 필드 유효성 체크",
				RestAssuredRestDocumentationWrapper.resourceDetails()
					.tag("회원 API")
					.summary("회원 가입"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("name").type(STRING).description("이름"),
					fieldWithPath("password").type(STRING).description("패스워드"),
					fieldWithPath("passwordConfirm").type(STRING).description("패스워드 확인"),
					fieldWithPath("phoneNumber").type(STRING).description("핸드폰 번호"),
					fieldWithPath("birthDate").type(STRING).description("생년월일")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("validation").type(OBJECT).description("유효하지 않은 필드")
				)))
			.contentType(JSON)
			.body(registerDto)
			.when()
				.post("/v1/member/register")
			.then().log().all()
				.statusCode(400);
	}

	@Test
	@DisplayName("2-1. 로그인 테스트")
	public void memberLoginTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("test@naver.com")
			.password("123456")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("로그인 API - 성공",
				RestAssuredRestDocumentationWrapper.resourceDetails()
					.tag("회원 API")
					.summary("로그인"),
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

	@Test
	@DisplayName("2-2. 로그인 아이디 혹은 패스워드 실패 테스트")
	public void memberLoginNotMatchTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("test@naver.com")
			.password("1234567")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("로그인 API - 실패: 로그인 정보",
				RestAssuredRestDocumentationWrapper.resourceDetails()
					.tag("회원 API")
					.summary("로그인"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("password").type(STRING).description("패스워드")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(memberLoginDto)
			.when()
			.post("/v1/member/login")
			.then().log().all()
			.statusCode(401);
	}

	@Test
	@DisplayName("2-3. 로그인 테스트")
	public void memberLoginValidTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("testnaver.com")
			.password("12345")
			.build();

		given(spec).log().all()
			.filter(RestAssuredRestDocumentationWrapper.document("로그인 API - 실패: 필드 유효성 체크",
				RestAssuredRestDocumentationWrapper.resourceDetails()
					.tag("회원 API")
					.summary("로그인"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("password").type(STRING).description("패스워드")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("validation").type(OBJECT).description("유효하지 않은 필드")
				)))
			.contentType(JSON)
			.body(memberLoginDto)
			.when()
			.post("/v1/member/login")
			.then().log().all()
			.statusCode(400);
	}

	// @Test
	// @DisplayName("4. 이메일 인증 요청 테스트")
	// public void memberEmailAuthRequestTest() throws Exception {
	// 	EmailAuthDto emailAuthDto = EmailAuthDto.builder()
	// 		.email("seongo0521@gmail.com")
	// 		.build();
	//
	// 	given(spec).log().all()
	// 		.filter(RestAssuredRestDocumentationWrapper.document("이메일 인증 요청 API",
	// 			RestAssuredRestDocumentationWrapper.resourceDetails()
	// 				.tag("회원 API")
	// 				.summary("이메일 인증 요청"),
	// 			requestFields(
	// 				fieldWithPath("email").type(STRING).description("이메일(계정)")
	// 			),
	// 			responseFields(
	// 				fieldWithPath("code").type(NUMBER).description("상태 코드"),
	// 				fieldWithPath("message").type(STRING).description("상태 메시지")
	// 			)))
	// 		.contentType(JSON)
	// 		.body(emailAuthDto)
	// 	.when()
	// 		.post("http://localhost:8089/v1/member/email/auth/request")
	// 	.then().log().all()
	// 		.statusCode(200);
	// }
}