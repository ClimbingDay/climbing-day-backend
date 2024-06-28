package com.climbingday.dto.crew;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CrewPostRegisterDto {
	@Size(max = 20, message = "게시글 제목은 20자 이하로 입력해주세요.")
	private String title;

	private String content;
}
