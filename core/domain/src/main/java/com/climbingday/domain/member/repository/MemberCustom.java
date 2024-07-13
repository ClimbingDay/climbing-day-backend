package com.climbingday.domain.member.repository;

import java.util.List;
import java.util.Optional;

import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberMyProfileDto;
import com.querydsl.core.Tuple;

public interface MemberCustom {
	List<MemberDto> getAllMember();

	List<Tuple> getMyPage(Long id);
}
