package com.climbingday.center.controller;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;

import java.io.File;
import java.io.IOException;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.center.service.CenterService;
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

	@SpyBean
	private CenterService centerService;

	@BeforeEach
	void setup(RestDocumentationContextProvider provider) {
		RestAssured.port = port;
		this.spec = new RequestSpecBuilder()
			.addFilter(documentationConfiguration(provider))
			.build();
	}

	@Test
	@DisplayName("1-1. 암장 등록 - 성공")
	public void getCenterRegister1Test() throws IOException {
		File file = new ClassPathResource("climbing-day-no-image.jpg").getFile();

		// 파일 업로드
		doReturn("https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg").when(centerService).uploadFile(any());

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
				requestParts(
					partWithName("profile_image").description("암장 이미지"),
					partWithName("center").description("암장 정보")
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("암장 고유번호")
				)))
			.header("Authorization", accessToken)
			.multiPart("profile_image", file)
			.multiPart("center", centerRegisterDto, "application/json")
			.contentType(MULTIPART_FORM_DATA_VALUE)
		.when()
			.post("/v1/center/register")
		.then().log().all()
			.statusCode(201);
	}

	@Test
	@DisplayName("1-2. 암장 등록 - 실패: 중복 암장이름")
	public void getCenterRegister2Test() throws IOException {
		File file = new ClassPathResource("climbing-day-no-image.jpg").getFile();

		// 파일 업로드
		doReturn("https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg").when(centerService).uploadFile(any());

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
				requestParts(
					partWithName("profile_image").description("암장 이미지"),
					partWithName("center").description("암장 정보")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.header("Authorization", accessToken)
			.multiPart("profile_image", file)
			.multiPart("center", centerRegisterDto, "application/json")
			.contentType(MULTIPART_FORM_DATA_VALUE)
		.when()
			.post("/v1/center/register")
		.then().log().all()
			.statusCode(409);
	}

	@Test
	@DisplayName("1-3. 암장 등록 - 실패: 필드 유효성")
	public void getCenterRegister3Test() throws IOException {
		File file = new ClassPathResource("climbing-day-no-image.jpg").getFile();

		// 파일 업로드
		doReturn("https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg").when(centerService).uploadFile(any());

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
				requestParts(
					partWithName("profile_image").description("암장 이미지"),
					partWithName("center").description("암장 정보")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("validation").type(OBJECT).description("유효하지 않은 필드")
				)))
			.header("Authorization", accessToken)
			.multiPart("profile_image", file)
			.multiPart("center", centerRegisterDto, "application/json")
			.contentType(MULTIPART_FORM_DATA_VALUE)
		.when()
			.post("/v1/center/register")
		.then().log().all()
			.statusCode(400);
	}

	@Test
	@DisplayName("2-1. 모든 암장 조회 - 성공")
	public void getCenterAll1Test() {
		given(spec).log().all()
			.filter(document("암장 조회 API - 성공",
				resourceDetails()
					.tag("암장 API")
					.summary("모든 암장 조회"),
				queryParameters(
					parameterWithName("page").description("조회할 페이지 번호").optional()
				),
				responseFields(
					fieldWithPath("code").type(NUMBER).description("상태 코드"),
					fieldWithPath("message").type(STRING).description("상태 메시지"),
					subsectionWithPath("data").type(OBJECT).description("암장 정보, 페이징 정보")
				)))
			.contentType(JSON)
			.queryParam("page", 0)
		.when()
			.get("/v1/center")
		.then().log().all()
			.statusCode(200);
	}

	@Test
	@DisplayName("2-2. 모든 암장 조회 - 실패: Parameter 누락")
	public void getCenterAll2Test() {
		given(spec).log().all()
			.filter(document("암장 조회 API - 실패: Parameter 누락",
				resourceDetails()
					.tag("암장 API")
					.summary("모든 암장 조회"),
				queryParameters(
					parameterWithName("page").description("조회할 페이지 번호").optional()
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지"),
					subsectionWithPath("missingParams").type(OBJECT).description("누락된 파라미터")
				)))
			.contentType(JSON)
		.when()
			.get("/v1/center")
		.then().log().all()
			.statusCode(400);
	}

	// @Test
	// @DisplayName("3-1. 암장 조회(이름) - 성공")
	// @Transactional
	// public void getCenterName1Test() throws IOException {
	// 	File file = new ClassPathResource("climbing-day-no-image.jpg").getFile();
	// 	String centerName = "클라이밍";
	//
	// 	// 파일 업로드
	// 	doReturn("https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg").when(centerService).uploadFile(any());
	//
	// 	CenterRegisterDto centerRegisterDto = CenterRegisterDto.builder()
	// 		.name("암장이름2")
	// 		.phoneNum("02-111-1111")
	// 		.address("암장주소")
	// 		.latitude(111.2222)
	// 		.longitude(111.2222)
	// 		.openTime("0:0")
	// 		.closeTime("23:59")
	// 		.description("설명")
	// 		.notice("공지")
	// 		.build();
	//
	// 	given(spec)
	// 		.header("Authorization", accessToken)
	// 		.multiPart("profile_image", file)
	// 		.multiPart("center", centerRegisterDto, "application/json")
	// 		.contentType(MULTIPART_FORM_DATA_VALUE)
	// 	.when()
	// 		.post("/v1/center/register");
	//
	// 	given(spec).log().all()
	// 		.filter(document("암장 조회 API - 성공",
	// 			resourceDetails()
	// 				.tag("암장 API")
	// 				.summary("암장 조회(이름)"),
	// 			pathParameters(
	// 				parameterWithName("centerName").description("암장 이름")
	// 			),
	// 			responseFields(
	// 				fieldWithPath("code").type(NUMBER).description("상태 코드"),
	// 				fieldWithPath("message").type(STRING).description("상태 메시지"),
	// 				subsectionWithPath("data").type(OBJECT).description("암장 정보")
	// 			)))
	// 		.contentType(JSON)
	// 	.when()
	// 		.get("/v1/center/{centerName}", centerName)
	// 	.then().log().all()
	// 		.statusCode(200);
	// }

	@Test
	@DisplayName("3-1. 암장 조회(이름) - 실패: 존재하지 않는 암장")
	public void getCenterName2Test() {
		String centerName = "암장이름2";

		given(spec).log().all()
			.filter(document("암장 조회 API - 실패: 존재하지 않는 암장",
				resourceDetails()
					.tag("암장 API")
					.summary("암장 조회(이름)"),
				pathParameters(
					parameterWithName("centerName").description("암장 이름")
				),
				responseFields(
					fieldWithPath("errorCode").type(NUMBER).description("상태 코드"),
					fieldWithPath("errorMessage").type(STRING).description("상태 메시지")
				)))
			.contentType(JSON)
		.when()
			.get("/v1/center/{centerName}", centerName)
		.then().log().all()
			.statusCode(400);
	}
}