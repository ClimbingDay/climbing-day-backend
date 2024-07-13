package com.climbingday.event.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.climbingday.domain.sigungu.repository.SigunguRepository;
import com.climbingday.dto.sigungu.SigunguDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
	private final SigunguRepository sigunguRepository;

	public List<SigunguDto> getSigunguList(String name) {
		return sigunguRepository.getSigunguList(name.trim());
	}
}
