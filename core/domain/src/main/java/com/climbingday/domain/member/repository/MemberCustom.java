package com.climbingday.domain.member.repository;

import java.util.List;
import java.util.Optional;

import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberMyPageDto;

public interface MemberCustom {
	List<MemberDto> getAllMember();

	Optional<MemberMyPageDto> getMyPage(Long id);
}
