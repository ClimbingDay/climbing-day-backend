package com.climbingday.dto.member;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberRegisterDto {
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "유효하지 않은 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 항목입니다.")
	private String email;

	@NotBlank(message = "이름은 필수 항목입니다.")
	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
	private String name;

	@NotBlank(message = "비밀번호는 필수 항목입니다.")
	@Size(min = 6, message = "비밀번호는 최소 6자 이상 입력해주세요.")
	private String password;

	@NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
	@Size(min = 6, message = "비밀번호 확인은 최소 6자 이상 입력해주세요.")
	private String passwordConfirm;

	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "핸드폰 번호를 올바르게 입력해주세요.")
	@NotBlank(message = "핸드폰 번호는 필수 항목입니다.")
	private String phoneNumber;
}
