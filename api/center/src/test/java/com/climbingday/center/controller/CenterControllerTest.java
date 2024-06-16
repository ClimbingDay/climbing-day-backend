package com.climbingday.center.controller;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.dto.center.CenterRegisterDto;
import com.climbingday.infra.config.TestConfig;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
class CenterControllerTest extends TestConfig {
	private RequestSpecification spec;

	private String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE4MTExMzk0LCJleHAiOjE4MTgxMTEzOTMsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.JitpZyXPoT9lol5axaROUX0i7Q8Fu_YoOKmRNwfjhDjSWI8VFa6iqueZKgWvz87oWUdIWLpp6ZoU0V3tPnhU6A";

	private String refreshToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwic3ViIjoiYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzE4MTExMzk0LCJleHAiOjE4MTgxMTEzOTMsInJvbGVzIjoiUk9MRV9BRE1JTiJ9.JitpZyXPoT9lol5axaROUX0i7Q8Fu_YoOKmRNwfjhDjSWI8VFa6iqueZKgWvz87oWUdIWLpp6ZoU0V3tPnhU6A";

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
	@DisplayName("1-1. 암장 등록 - 성공")
	@Transactional
	public void getCenterRegister1Test() {
		CenterRegisterDto centerRegisterDto = CenterRegisterDto.builder()
			.name("암장이름")
			.phoneNum("02-111-1111")
			.address("암장주소")
			.latitude(111.2222)
			.longitude(111.2222)
			.openTime("0:0")
			.closeTime("23:59")
			.description("설명")
			.notice("공지")
			.build();

		given(spec).log().all()
			.filter(document("암장 등록 API - 성공",
				resourceDetails()
					.tag("암장 API")
					.summary("암장 등록"),
				requestFields(
					fieldWithPath("name").type(STRING).description("암장 이름"),
					fieldWithPath("phoneNum").type(STRING).description("전화번호"),
					fieldWithPath("address").type(STRING).description("주소"),
					fieldWithPath("latitude").type(NUMBER).description("위도"),
					fieldWithPath("longitude").type(NUMBER).description("경도"),
					fieldWithPath("openTime").type(STRING).description("영업시작시간"),
					fieldWithPath("closeTime").type(STRING).description("영업종료시간"),
					fieldWithPath("description").type(STRING).description("소개"),
					fieldWithPath("notice").type(STRING).description("공지사항")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("암장 고유번호")
				)))
			.header("Authorization", accessToken)
			.body(centerRegisterDto)
			.contentType(JSON)
		.when()
			.post("/v1/center/register")
		.then().log().all()
			.statusCode(201);
	}

	@Test
	@DisplayName("1-2. 암장 등록 - 실패: 중복 암장이름")
	@Transactional
	public void getCenterRegister2Test() {
		CenterRegisterDto centerRegisterDto = CenterRegisterDto.builder()
			.name("암장이름")
			.phoneNum("02-111-1111")
			.address("암장주소")
			.latitude(111.2222)
			.longitude(111.2222)
			.openTime("0:0")
			.closeTime("23:59")
			.description("설명")
			.notice("공지")
			.build();

		given(spec).log().all()
			.filter(document("암장 등록 API - 실패: 중복 암장이름",
				resourceDetails()
					.tag("암장 API")
					.summary("암장 등록"),
				requestFields(
					fieldWithPath("name").type(STRING).description("암장 이름"),
					fieldWithPath("phoneNum").type(STRING).description("전화번호"),
					fieldWithPath("address").type(STRING).description("주소"),
					fieldWithPath("latitude").type(NUMBER).description("위도"),
					fieldWithPath("longitude").type(NUMBER).description("경도"),
					fieldWithPath("openTime").type(STRING).description("영업시작시간"),
					fieldWithPath("closeTime").type(STRING).description("영업종료시간"),
					fieldWithPath("description").type(STRING).description("소개"),
					fieldWithPath("notice").type(STRING).description("공지사항")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.header("Authorization", accessToken)
			.body(centerRegisterDto)
			.contentType(JSON)
		.when()
			.post("/v1/center/register")
		.then().log().all()
			.statusCode(409);
	}

	@Test
	@DisplayName("1-3. 암장 등록 - 실패: 필드 유효성")
	@Transactional
	public void getCenterRegister3Test() {
		CenterRegisterDto centerRegisterDto = CenterRegisterDto.builder()
			.name("")
			.phoneNum("")
			.address("")
			.latitude(111.2222)
			.longitude(111.2222)
			.openTime("0:0")
			.closeTime("23:59")
			.description("")
			.notice("")
			.build();

		given(spec).log().all()
			.filter(document("암장 등록 API - 실패: 필드 유효성",
				resourceDetails()
					.tag("암장 API")
					.summary("암장 등록"),
				requestFields(
					fieldWithPath("name").type(STRING).description("암장 이름"),
					fieldWithPath("phoneNum").type(STRING).description("전화번호"),
					fieldWithPath("address").type(STRING).description("주소"),
					fieldWithPath("latitude").type(NUMBER).description("위도"),
					fieldWithPath("longitude").type(NUMBER).description("경도"),
					fieldWithPath("openTime").type(STRING).description("영업시작시간"),
					fieldWithPath("closeTime").type(STRING).description("영업종료시간"),
					fieldWithPath("description").type(STRING).description("소개"),
					fieldWithPath("notice").type(STRING).description("공지사항")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("validation").type(OBJECT).description("유효하지 않은 필드")
				)))
			.header("Authorization", accessToken)
			.body(centerRegisterDto)
			.contentType(JSON)
		.when()
			.post("/v1/center/register")
		.then().log().all()
			.statusCode(400);
	}

	@Test
	@DisplayName("2-1. 암장 조회 - 성공")
	@Transactional
	public void getCenterAll1Test() {
		given(spec).log().all()
			.filter(document("암장 조회 API - 성공",
				resourceDetails()
					.tag("암장 API")
					.summary("암장 조회"),
				queryParameters(
					parameterWithName("page").description("조회할 페이지 번호").optional()
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					subsectionWithPath("data").type(OBJECT).description("암장 정보, 페이징 정보")
				)))
			.contentType(JSON)
			.header("Authorization", refreshToken)
			.queryParam("page", 0)
		.when()
			.get("/v1/center")
		.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("2-2. 암장 조회 - 실패: Parameter 누락")
	@Transactional
	public void getCenterAll2Test() {
		given(spec).log().all()
			.filter(document("암장 조회 API - 실패: Parameter 누락",
				resourceDetails()
					.tag("암장 API")
					.summary("암장 조회"),
				queryParameters(
					parameterWithName("page").description("조회할 페이지 번호").optional()
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("missingParams").type(OBJECT).description("누락된 파라미터")
				)))
			.contentType(JSON)
			.header("Authorization", refreshToken)
		.when()
			.get("/v1/center")
		.then().log().all()
			.statusCode(400);
	}
}