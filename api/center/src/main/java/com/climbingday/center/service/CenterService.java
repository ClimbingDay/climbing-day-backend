package com.climbingday.center.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.climbingday.domain.center.repository.CenterRepository;
import com.climbingday.dto.center.CenterDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CenterService {
	private final CenterRepository centerRepository;

	/**
	 * Center 조회
	 */
	public Page<CenterDto> getCenterPage(Pageable pageable) {
		return centerRepository.findAllCenter(pageable);
	}
}
