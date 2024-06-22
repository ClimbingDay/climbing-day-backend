package com.climbingday.member.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.crew.repository.CrewRepository;
import com.climbingday.dto.crew.CrewProfileDto;
import com.climbingday.security.service.UserDetailsImpl;

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

	/**
	 * 나의 크루 조회(프로필)
	 */
	@Transactional(readOnly = true)
	public List<CrewProfileDto> getMyCrewProfile(UserDetailsImpl userDetails) {
		long userId = userDetails.getId();

		return crewRepository.getMyCrewProfile(userId);
	}
}
