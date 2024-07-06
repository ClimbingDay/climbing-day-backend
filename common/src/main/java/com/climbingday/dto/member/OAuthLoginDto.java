package com.climbingday.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OAuthLoginDto {
	@NotBlank(message = "엑세스 토큰은 필수 항목입니다.")
	private String accessToken;
}
