package com.climbingday.dto.member;

import com.climbingday.dto.terms.TermsListDto;
import com.climbingday.enums.member.EProviders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "유효하지 않은 이메일 형식입니다.")
	private String email;

	@NotBlank(message = "닉네임은 필수 항목입니다.")
	@Size(min = 2, max = 8, message = "닉네임은 2자 이상 8자 이하로 입력해주세요.")
	private String nickName;

	@NotBlank(message = "비밀번호는 필수 항목입니다.")
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "유효하지 않은 비밀번호 형식입니다.")
	private String password;

	@NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "유효하지 않은 비밀번호 형식입니다.")
	private String passwordConfirm;

	@NotBlank(message = "핸드폰 번호는 필수 항목입니다.")
	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "핸드폰 번호를 올바르게 입력해주세요.")
	private String phoneNumber;

	@NotBlank(message = "생년월일은 필수 항목입니다")
	@Pattern(regexp = "^(?:(?:19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$", message = "생년월일을 올바르게 입력해주세요.")
	private String birthDate;

	@Size(max = 25, message = "회원 소개글은 25자 이하로 입력해주세요.")
	private String introduce;

	@Valid
	@NotNull(message = "이용약관 동의 정보는 필수 항목입니다.")
	private TermsListDto terms;

	private EProviders provider;
}
