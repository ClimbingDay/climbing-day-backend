package com.climbingday.domain.crew.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.climbingday.dto.crew.CrewProfileDto;

public interface CrewCustom {
	Page<CrewProfileDto> getAllCrewProfile(Pageable pageable);
}
