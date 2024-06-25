package com.climbingday.domain.crew.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.climbingday.dto.crew.CrewPostDto;

public interface CrewPostCustom {
	Page<CrewPostDto> getAllPost(Pageable pageable);
}
