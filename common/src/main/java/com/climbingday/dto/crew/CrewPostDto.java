package com.climbingday.dto.crew;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CrewPostDto {
	// 크루 게시글 번호
	private Long id;

	// 크루 게시글 제목
	private String title;

	// 크루 게시글 내용
	private String content;

	// 좋아요 갯수
	private Integer likeCount;

	// 댓글 갯수
	private Integer commentCount;

	// 작성자
	private String createBy;

	// 작성일
	private LocalDateTime createdDate;
}
