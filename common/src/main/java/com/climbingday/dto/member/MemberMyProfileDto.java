package com.climbingday.dto.member;

import java.util.ArrayList;
import java.util.List;

import com.climbingday.dto.crew.CrewProfileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberMyProfileDto {
	private Long id;

	private String nickName;

	private String profileImage;

	private String introduce;

	@Builder.Default
	private List<CrewProfileDto> crew = new ArrayList<>();
}
