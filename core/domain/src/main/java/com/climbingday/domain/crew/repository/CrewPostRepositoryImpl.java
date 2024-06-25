package com.climbingday.domain.crew.repository;

import static com.climbingday.domain.crew.QCrewComment.*;
import static com.climbingday.domain.crew.QCrewPost.*;
import static com.climbingday.domain.crew.QCrewPostLike.*;
import static com.climbingday.domain.member.QMember.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.climbingday.dto.crew.CrewPostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CrewPostRepositoryImpl implements CrewPostCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<CrewPostDto> getAllPost(Pageable pageable) {

		List<CrewPostDto> generalPostList = queryFactory
			.select(Projections.constructor(CrewPostDto.class,
				crewPost.id,
				crewPost.title,
				crewPost.content,
				crewPostLike.countDistinct().castToNum(Integer.class),
				crewComment.countDistinct().castToNum(Integer.class),
				member.nickName,
				crewPost.createdDate
			))
			.from(crewPost)
			.leftJoin(crewPostLike).on(crewPostLike.crewPost.id.eq(crewPost.id))
			.leftJoin(crewComment).on(crewComment.crewPost.id.eq(crewPost.id))
			.leftJoin(member).on(crewPost.member.id.eq(member.id))
			.groupBy(crewPost.id, member.nickName)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(queryFactory
			.select(crewPost.count())
			.from(crewPost)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(generalPostList, pageable, total);
	}
}
