package com.climbingday.domain.member.repository;

import static com.climbingday.domain.crew.QCrew.*;
import static com.climbingday.domain.member.QMember.*;
import static com.climbingday.domain.memberCrew.QMemberCrew.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberMyPageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

	@Override
	public Optional<MemberMyPageDto> getMyPage(Long id) {
		MemberMyPageDto memberMyPageDto = queryFactory
			.select(Projections.constructor(MemberMyPageDto.class,
				member.id,
				member.nickName,
				member.profileImage,
				member.introduce,
				crew.name
			))
			.from(member)
			.leftJoin(memberCrew).on(member.id.eq(memberCrew.member.id))
			.leftJoin(crew).on(memberCrew.crew.id.eq(crew.id))
			.where(memberId(id))
			.fetchOne();

		return Optional.ofNullable(memberMyPageDto);
	}

	private BooleanExpression memberId(Long id) {
		return member.id.eq(id);
	}
}
