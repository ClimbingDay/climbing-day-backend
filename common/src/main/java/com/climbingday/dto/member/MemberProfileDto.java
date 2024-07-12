package com.climbingday.dto.member;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.HashMap;
import java.util.Map;

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

	public Map<String, String> getNonNullFields() {
		Map<String, String> nonNullFields = new HashMap<>();

		if(nickName != null) nonNullFields.put("nickName", nickName);
		if(password != null) {
			if(!password.equals(passwordConfirm)) {
				throw new IllegalArgumentException(NOT_MATCHED_PASSWORD.getErrorMessage());
			}
			nonNullFields.put("password", password);
		}
		if(introduce != null) nonNullFields.put("introduce", introduce);

		return nonNullFields;
	}
}
