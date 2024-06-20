package com.climbingday.domain.center.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.climbingday.dto.center.CenterDto;

public interface CenterCustom {
	Page<CenterDto> findAllCenter(Pageable pageable);

	Optional<CenterDto> findByName(String centerName);
}
