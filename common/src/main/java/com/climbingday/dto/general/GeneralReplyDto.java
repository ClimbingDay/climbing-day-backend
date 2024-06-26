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

	private String content;

	private String createdBy;

	private LocalDateTime createdDate;

	private Integer likeCount;
}
