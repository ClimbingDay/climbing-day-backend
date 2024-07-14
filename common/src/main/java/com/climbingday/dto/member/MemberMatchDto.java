package com.climbingday.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberMatchDto {
	private String nickName;

	private String profileImage;

	private String sigunguName;
}
