package com.climbingday.dto.general;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class GeneralCommentDto {
	private Long id;

	private Long postId;

	private String content;

	private Integer likeCount;

	private Integer replyCount;

	private String createdBy;

	private LocalDateTime createdDate;

	List<GeneralReplyDto> replies = new ArrayList<>();
}
