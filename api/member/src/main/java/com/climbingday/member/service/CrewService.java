package com.climbingday.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.crew.repository.CrewRepository;
import com.climbingday.dto.crew.CrewProfileDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewService {
	private final CrewRepository crewRepository;

	/**
	 * 모든 크루 조회(프로필)
	 */
	@Transactional(readOnly = true)
	public Page<CrewProfileDto> getCrewProfilePage(Pageable pageable) {
		return crewRepository.getAllCrewProfile(pageable);
	}
}
