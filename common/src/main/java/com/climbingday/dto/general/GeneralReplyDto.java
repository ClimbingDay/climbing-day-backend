package com.climbingday.dto.general;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class GeneralReplyDto {
	private Long id;

	private Long commentId;

	private String content;

	private Integer likeCount;

	private String createdBy;

	private LocalDateTime createdDate;
}
