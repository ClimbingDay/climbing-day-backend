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

	private LocalDateTime createDate;

	private Integer likeCount;

	private Integer commentCount;

	List<GeneralReplyDto> replies = new ArrayList<>();
}
