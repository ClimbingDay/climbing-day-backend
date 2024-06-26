package com.climbingday.domain.general.repository;

import java.util.List;

import com.climbingday.dto.general.GeneralCommentDto;

public interface GeneralCommentCustom {
	List<GeneralCommentDto> getCommentsForPost(Long postId);
}
