package com.climbingday.dto.general;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class GeneralPostDto {
	// 일반 게시글 번호
	private Long id;

	// 일반 게시글 제목
	private String title;

	// 일반 게시글 내용
	private String content;

	// 좋아요 갯수
	private Integer likeCount;

	// 댓글 갯수
	private Integer commentCount;

	// 작성자
	private String createdBy;

	// 작성일
	private LocalDateTime createdDate;
}
