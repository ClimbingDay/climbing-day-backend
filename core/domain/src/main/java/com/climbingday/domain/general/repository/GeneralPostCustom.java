package com.climbingday.domain.general.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.climbingday.dto.general.GeneralPostDto;

public interface GeneralPostCustom {
	Page<GeneralPostDto> getAllPost(Pageable pageable);
}
