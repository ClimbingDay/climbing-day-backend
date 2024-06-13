package com.climbingday.member.controller;

import static com.climbingday.enums.MailErrorCode.*;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
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

import com.climbingday.dto.member.EmailAuthDto;
import com.climbingday.dto.member.EmailDto;
import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.infra.config.TestConfig;
import com.climbingday.member.exception.MemberException;
import com.climbingday.member.service.MemberService;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
class MemberControllerTest extends TestConfig {

	private RequestSpecification spec;

	@SpyBean
	private MemberService memberService;

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
	@DisplayName("1-1. 회원가입 테스트 - 성공")
	public void memberRegisterTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@naver.com")
			.nickName("test")
			.password("a12345!@#")
			.passwordConfirm("a12345!@#")
			.phoneNumber("010-1234-5678")
			.birthDate("2000-11-11")
			.build();

		given(spec).log().all()
			.filter(document("회원 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("회원 가입"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("nickName").type(STRING).description("닉네임"),
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
	@DisplayName("1-2. 회원가입 테스트 - 실패: 중복 회원")
	public void memberRegisterDuplicatedTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@naver.com")
			.nickName("test")
			.password("a12345!@#")
			.passwordConfirm("a12345!@#")
			.phoneNumber("010-1234-5678")
			.birthDate("2000-11-11")
			.build();

		given(spec).log().all()
			.filter(document("회원 API - 실패: 중복 회원",
				resourceDetails()
					.tag("회원 API")
					.summary("회원 가입"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("nickName").type(STRING).description("닉네임"),
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
	@DisplayName("1-3. 회원가입 테스트 - 실패: 필드 유효성")
	public void memberRegisterValidTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("testnaver.com")
			.nickName("")
			.password("12345")
			.passwordConfirm("12345")
			.phoneNumber("0101234-5678")
			.birthDate("200011-11")
			.build();

		given(spec).log().all()
			.filter(document("회원 API - 실패: 필드 유효성",
				resourceDetails()
					.tag("회원 API")
					.summary("회원 가입"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("nickName").type(STRING).description("닉네임"),
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
	@DisplayName("2-1. 로그인 테스트 - 성공")
	public void memberLoginTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("test@naver.com")
			.password("a12345!@#")
			.build();

		given(spec).log().all()
			.filter(document("로그인 API - 성공",
				resourceDetails()
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
	@DisplayName("2-2. 로그인 테스트 - 실패: 아이디, 비밀번호 잘못 입력")
	public void memberLoginNotMatchTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("test@naver.com")
			.password("a12345!@#@")
			.build();

		given(spec).log().all()
			.filter(document("로그인 API - 실패: 로그인 정보",
				resourceDetails()
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
	@DisplayName("2-3. 로그인 테스트 - 실패: 필드 유효성")
	public void memberLoginValidTest() throws Exception {
		MemberLoginDto memberLoginDto = MemberLoginDto.builder()
			.email("testnaver.com")
			.password("12345")
			.build();

		given(spec).log().all()
			.filter(document("로그인 API - 실패: 필드 유효성",
				resourceDetails()
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

	@Test
	@DisplayName("3-1. 이메일 인증 코드 요청 테스트 - 성공")
	public void memberEmailAuthRequestTest() throws Exception {
		EmailDto emailAuthDto = EmailDto.builder()
			.email("seongo0521@gmail.com")
			.build();

		// 메일 서비스 호출
		doNothing().when(memberService).sendEmailVerification(anyString(), anyString());

		given(spec).log().all()
			.filter(document("이메일 인증 코드 요청 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 요청"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
		.when()
			.post("/v1/member/email/auth/request")
		.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("3-2. 이메일 인증 코드 요청 테스트 - 실패: 이미 등록 된 이메일")
	public void memberEmailAuthEmailRequestDuplicatedTest() throws Exception {
		EmailDto emailAuthDto = EmailDto.builder()
			.email("test@naver.com")
			.build();

		// 메일 서비스 호출
		doNothing().when(memberService).sendEmailVerification(anyString(), anyString());

		given(spec).log().all()
			.filter(document("이메일 인증 코드 요청 API - 실패: 이미 등록 된 이메일",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 요청"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/request")
			.then().log().all()
			.statusCode(409);
	}

	@Test
	@DisplayName("3-3. 이메일 인증 코드 요청 테스트 - 실패: 메일 서버 이슈")
	public void memberEmailAuthRequestFailTest() throws Exception {
		EmailDto emailAuthDto = EmailDto.builder()
			.email("seongo0521@gmail.com")
			.build();

		// 메일 서비스 호출
		doThrow(new MemberException(UNABLE_TO_SEND_EMAIL)).when(memberService).sendEmailVerification(anyString(), anyString());

		given(spec).log().all()
			.filter(document("이메일 인증 코드 요청 API - 실패: 이메일 서버 이슈",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 요청"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/request")
			.then().log().all()
			.statusCode(504);
	}

	@Test
	@DisplayName("3-4. 이메일 인증 코드 요청 테스트 - 실패: 필드 유효성")
	public void memberEmailAuthRequestValidTest() throws Exception {
		EmailDto emailAuthDto = EmailDto.builder()
			.email("")
			.build();

		// 메일 서비스 호출
		doThrow(new MemberException(UNABLE_TO_SEND_EMAIL)).when(memberService).sendEmailVerification(anyString(), anyString());

		given(spec).log().all()
			.filter(document("이메일 인증 코드 요청 API - 실패: 필드 유효성",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 요청"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("validation").type(OBJECT).description("유효하지 않은 필드")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/request")
			.then().log().all()
			.statusCode(400);
	}

	@Test
	@DisplayName("4-1. 이메일 인증 코드 확인 테스트 - 성공")
	public void memberEmailAuthConfirmTest() throws Exception {
		EmailAuthDto emailAuthDto = EmailAuthDto.builder()
			.email("seongo0521@gmail.com")
			.authCode("111111")
			.build();

		// 이메일 인증 코드 확인
		doNothing().when(memberService).emailAuthConfirm(any());

		given(spec).log().all()
			.filter(document("이메일 인증 코드 확인 API - 성공",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 확인"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("authCode").type(STRING).description("인증 코드")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/confirm")
			.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("4-2. 이메일 인증 코드 확인 테스트 - 실패: 인증 코드 요청 필요")
	public void memberEmailAuthConfirmFail1Test() throws Exception {
		EmailAuthDto emailAuthDto = EmailAuthDto.builder()
			.email("seongo0521@gmail.com")
			.authCode("111111")
			.build();

		given(spec).log().all()
			.filter(document("이메일 인증 코드 확인 API - 실패: 인증 코드 요청 필요",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 확인"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("authCode").type(STRING).description("인증 코드")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/confirm")
			.then().log().all()
			.statusCode(400);
	}

	@Test
	@DisplayName("4-3. 이메일 인증 코드 확인 테스트 - 실패: 인증 코드 불일치")
	public void memberEmailAuthConfirmFail2Test() throws Exception {
		EmailAuthDto emailAuthDto = EmailAuthDto.builder()
			.email("seongo0521@gmail.com")
			.authCode("111111")
			.build();

		doThrow(new MemberException(NOT_MATCHED_AUTH_CODE)).when(memberService).emailAuthConfirm(any());

		given(spec).log().all()
			.filter(document("이메일 인증 코드 확인 API - 실패: 인증 코드 불일치",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 확인"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("authCode").type(STRING).description("인증 코드")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/confirm")
			.then().log().all()
			.statusCode(401);
	}

	@Test
	@DisplayName("4-3. 이메일 인증 코드 확인 테스트 - 실패: 필드 유효성")
	public void memberEmailAuthConfirmFail3Test() throws Exception {
		EmailAuthDto emailAuthDto = EmailAuthDto.builder()
			.email("")
			.authCode("")
			.build();

		given(spec).log().all()
			.filter(document("이메일 인증 코드 확인 API - 실패: 필드 유효성",
				resourceDetails()
					.tag("회원 API")
					.summary("이메일 인증 코드 확인"),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일(계정)"),
					fieldWithPath("authCode").type(STRING).description("인증 코드")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("validation").type(OBJECT).description("유효하지 않은 필드")
				)))
			.contentType(JSON)
			.body(emailAuthDto)
			.when()
			.post("/v1/member/email/auth/confirm")
			.then().log().all()
			.statusCode(400);
	}
}