package com.climbingday.domain.general.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.climbingday.dto.general.GeneralPostDetailDto;
import com.climbingday.dto.general.GeneralPostDto;

public interface GeneralPostCustom {
	Page<GeneralPostDto> getAllPost(Pageable pageable);

	Optional<GeneralPostDetailDto> getPost(Long postId);
}
