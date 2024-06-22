package com.climbingday.domain.crew.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.climbingday.dto.crew.CrewProfileDto;

public interface CrewCustom {
	Page<CrewProfileDto> getAllCrewProfile(Pageable pageable);

	List<CrewProfileDto> getMyCrewProfile(Long id);
}
