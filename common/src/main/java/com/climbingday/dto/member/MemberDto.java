package com.climbingday.dto.member;

import com.climbingday.enums.member.ERoles;
import com.climbingday.enums.member.EStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberDto {
	private long id;

	private String email;

	private EStatus status;

	private ERoles roles;
}
