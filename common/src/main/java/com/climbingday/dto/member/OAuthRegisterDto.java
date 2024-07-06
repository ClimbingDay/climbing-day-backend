package com.climbingday.dto.member;

import com.climbingday.dto.terms.TermsListDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OAuthRegisterDto {
	@NotBlank(message = "엑세스 토큰은 필수 항목입니다.")
	private String accessToken;

	@NotBlank(message = "닉네임은 필수 항목입니다.")
	@Size(min = 2, max = 8, message = "닉네임은 2자 이상 8자 이하로 입력해주세요.")
	private String nickName;

	@Valid
	@NotNull(message = "이용약관 동의 정보는 필수 항목입니다.")
	private TermsListDto terms;
}
