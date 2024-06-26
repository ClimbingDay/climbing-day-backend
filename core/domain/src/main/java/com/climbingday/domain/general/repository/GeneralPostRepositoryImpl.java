package com.climbingday.domain.general.repository;

import static com.climbingday.domain.general.QGeneralComment.*;
import static com.climbingday.domain.general.QGeneralPost.*;
import static com.climbingday.domain.general.QGeneralPostLike.*;
import static com.climbingday.domain.member.QMember.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.climbingday.dto.general.GeneralPostDetailDto;
import com.climbingday.dto.general.GeneralPostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GeneralPostRepositoryImpl implements GeneralPostCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GeneralPostDto> getAllPost(Pageable pageable) {

		List<GeneralPostDto> generalPostList =
			selectPostQuery()
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = Optional.ofNullable(queryFactory
			.select(generalPost.count())
			.from(generalPost)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(generalPostList, pageable, total);
	}

	@Override
	public Optional<GeneralPostDetailDto> getPost(Long postId) {
		GeneralPostDetailDto generalPostDto = selectPostDetailQuery()
			.where(generalPost.id.eq(postId))
			.fetchOne();
		return Optional.ofNullable(generalPostDto);
	}

	// 일반 게시판 조회 쿼리
	private JPQLQuery<GeneralPostDto> selectPostQuery() {
		return queryFactory
			.select(Projections.constructor(GeneralPostDto.class,
				generalPost.id,
				generalPost.title,
				generalPost.content,
				generalPostLike.countDistinct().castToNum(Integer.class),
				generalComment.countDistinct().castToNum(Integer.class),
				member.nickName,
				generalPost.createdDate
			))
			.from(generalPost)
			.leftJoin(generalPostLike).on(generalPostLike.generalPost.id.eq(generalPost.id))
			.leftJoin(generalComment).on(generalComment.generalPost.id.eq(generalPost.id))
			.leftJoin(member).on(generalPost.member.id.eq(member.id))
			.groupBy(generalPost.id, member.nickName);
	}

	// 일반 게시판 상세 조회 쿼리
	private JPQLQuery<GeneralPostDetailDto> selectPostDetailQuery() {
		return queryFactory
			.select(Projections.bean(GeneralPostDetailDto.class,
				generalPost.id,
				generalPost.title,
				generalPost.content,
				generalPostLike.countDistinct().castToNum(Integer.class).as("likeCount"),
				generalComment.countDistinct().castToNum(Integer.class).as("commentCount"),
				member.nickName.as("createdBy"),
				generalPost.createdDate
			))
			.from(generalPost)
			.leftJoin(generalPostLike).on(generalPostLike.generalPost.id.eq(generalPost.id))
			.leftJoin(generalComment).on(generalComment.generalPost.id.eq(generalPost.id))
			.leftJoin(member).on(generalPost.member.id.eq(member.id))
			.groupBy(generalPost.id, member.nickName);
	}
}
