package com.climbingday.dto.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class GeneralPostDetailDto {
	GeneralPostDto post;

	GeneralCommentDto comment;
}
