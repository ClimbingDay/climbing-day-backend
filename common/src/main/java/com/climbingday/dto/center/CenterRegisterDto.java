package com.climbingday.dto.center;

import java.time.LocalTime;

import com.climbingday.annotation.DoubleValid;
import com.climbingday.annotation.LocalTimeValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CenterRegisterDto {
	@NotBlank(message = "암장이름은 필수 항목입니다.")
	@Size(max = 40, message = "암장이름은 40자 이하로 입력해주세요.")
	private String name;

	@Pattern(regexp = "^(0(?:2|3[1-3]|4[1-4]|5[1-5]|6[1-4])-[1-9]\\d{2,3}-\\d{4}|01[016789]-[1-9]\\d{2,3}-\\d{4})$",
		message = "전화번호를 올바르게 입력해주세요")
	@NotBlank(message = "전화번호는 필수 항목입니다.")
	private String phoneNum;

	@NotBlank(message = "주소는 필수 항목입니다.")
	private String address;

	@DoubleValid(message = "위도는 필수 항목입니다.")
	private double latitude;

	@DoubleValid(message = "경도는 필수 항목입니다.")
	private double longitude;

	@LocalTimeValid(message = "영업시작시간을 올바르게 입력해주세요.")
	@Builder.Default
	private String openTime = "0:0";

	@LocalTimeValid(message = "영업종료시간을 올바르게 입력해주세요.")
	@Builder.Default
	private String closeTime = "0:0";

	@Builder.Default
	private String description = "";

	@Builder.Default
	private String notice = "";
}
