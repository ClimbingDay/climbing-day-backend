package com.climbingday.domain.general;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GeneralReply {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "general_reply_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_comment_id")
	private GeneralComment generalComment;			// 일반 게시글

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;						// 회원

	@Lob
	private String content;						// 일반 게시글 내용

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;			// 생성일

	@LastModifiedDate
	private LocalDateTime updatedDate; 			// 수정일
}
