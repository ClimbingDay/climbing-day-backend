package com.climbingday.domain.crew;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.climbingday.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CrewCommentLike {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crew_comment_like_id")
	private Long id;							// 크루 게시글 좋아요 id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crew_comment_id")
	private CrewComment crewComment;			// 크루 게시글

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;						// 회원

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;			// 생성일
}
