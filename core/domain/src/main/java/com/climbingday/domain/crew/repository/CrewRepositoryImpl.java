package com.climbingday.domain.crew.repository;

import static com.climbingday.domain.crew.QCrew.*;
import static com.climbingday.domain.member.QMember.*;
import static com.climbingday.domain.memberCrew.QMemberCrew.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.climbingday.dto.crew.CrewProfileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CrewRepositoryImpl implements CrewCustom {

	private final JPAQueryFactory queryFactory;

	public Page<CrewProfileDto> getAllCrewProfile(Pageable pageable) {
		List<CrewProfileDto> crewProfileDtoList = selectCrewNameProfile()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(queryFactory
			.select(crew.count())
			.from(crew)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(crewProfileDtoList, pageable, total);
	}

	public List<CrewProfileDto> getMyCrewProfile(Long id) {
		return selectCrewNameProfile()
			.join(memberCrew).on(crew.id.eq(memberCrew.crew.id))
			.join(member).on(memberCrew.member.id.eq(member.id))
			.where(member.id.eq(id))
			.fetch();
	}

	private JPQLQuery<CrewProfileDto> selectCrewNameProfile() {
		return queryFactory.select(Projections.constructor(CrewProfileDto.class,
			crew.id,
			crew.name,
			crew.profileImage
		)).from(crew);
	}
}
