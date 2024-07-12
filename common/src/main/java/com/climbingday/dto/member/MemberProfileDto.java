package com.climbingday.dto.member;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberProfileDto {
	@Size(min = 2, max = 8, message = "닉네임은 2자 이상 8자 이하로 입력해주세요.")
	private String nickName;

	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "유효하지 않은 비밀번호 형식입니다.")
	private String password;

	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "유효하지 않은 비밀번호 형식입니다.")
	private String passwordConfirm;

	@Size(max = 25, message = "회원 소개글은 25자 이하로 입력해주세요.")
	private String introduce;
}
