package com.climbingday.domain.general.repository;

import static com.climbingday.domain.general.QGeneralComment.*;
import static com.climbingday.domain.general.QGeneralCommentLike.*;
import static com.climbingday.domain.general.QGeneralReply.*;
import static com.climbingday.domain.general.QGeneralReplyLike.*;
import static com.climbingday.domain.member.QMember.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.climbingday.dto.general.GeneralCommentDto;
import com.climbingday.dto.general.GeneralReplyDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GeneralCommentRepositoryImpl implements GeneralCommentCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<GeneralCommentDto> getCommentsForPost(Long postId) {
		// 댓글 조회
		List<GeneralCommentDto> comments = queryFactory
			.select(Projections.bean(GeneralCommentDto.class,
				generalComment.id,
				generalComment.generalPost.id.as("postId"),
				generalComment.content,
				generalCommentLike.count().castToNum(Integer.class).as("likeCount"),
				generalReply.count().castToNum(Integer.class).as("replyCount"),
				member.nickName.as("createdBy"),
				generalComment.createdDate
			))
			.from(generalComment)
			.leftJoin(generalCommentLike).on(generalCommentLike.generalComment.id.eq(generalComment.id))
			.leftJoin(generalReply).on(generalReply.generalComment.id.eq(generalComment.id))
			.leftJoin(member).on(generalComment.member.id.eq(member.id))
			.where(generalComment.generalPost.id.eq(postId))
			.groupBy(generalComment.id, member.nickName)
			.fetch();

		// 대댓글 조회 및 매핑
		Map<Long, List<GeneralReplyDto>> repliesMap = getRepliesMap(postId);

		comments.forEach(comment -> comment.setReplies(repliesMap.get(comment.getId())));

		return comments;
	}

	// 일반 게시글 대댓글 조회 쿼리
	private Map<Long, List<GeneralReplyDto>> getRepliesMap(Long postId) {
		List<GeneralReplyDto> replies = queryFactory
			.select(Projections.constructor(GeneralReplyDto.class,
				generalReply.generalComment.id,
				generalReply.id,
				generalReply.content,
				generalReplyLike.count().castToNum(Integer.class),
				member.nickName,
				generalReply.createdDate
			))
			.from(generalReply)
			.leftJoin(generalReplyLike).on(generalReplyLike.generalReply.id.eq(generalReply.id))
			.leftJoin(member).on(generalReply.member.id.eq(member.id))
			.where(generalReply.generalComment.generalPost.id.eq(postId))
			.groupBy(generalReply.id, member.nickName, generalReply.generalComment.id)
			.fetch();

		return replies.stream().collect(Collectors.groupingBy(GeneralReplyDto::getCommentId));
	}
}
