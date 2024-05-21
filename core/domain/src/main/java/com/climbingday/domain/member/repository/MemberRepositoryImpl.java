package com.climbingday.domain.member.repository;

import static com.climbingday.domain.member.QMember.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.climbingday.domain.member.QMember;
import com.climbingday.dto.member.MemberDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<MemberDto> getAllMember() {
		return queryFactory
			.select(Projections.constructor(MemberDto.class,
				member.id,
				member.email,
				member.status,
				member.roles
				))
			.from(member)
			.fetch();
	}
}
