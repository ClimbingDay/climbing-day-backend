package com.climbingday.member.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.climbingday.member.dto.request.MemberRegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void memberRegisterTest() throws Exception {
		MemberRegisterDto registerDto = MemberRegisterDto.builder()
			.email("test@gmail.com")
			.name("테스트")
			.password("123456")
			.passwordConfirm("123456")
			.phoneNumber(List.of("010", "1234", "5678"))
			.build();

		this.mockMvc.perform(post("/register")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("테스트"))
			.andDo(document("회원 등록",
				responseFields(
					fieldWithPath("code").description("회원 등록이 정상"),
					fieldWithPath("message").description("정상적으로 생성되었습니다."),
					fieldWithPath("data").description("생성된 회원이 고유 번호")
				)));
	}

}