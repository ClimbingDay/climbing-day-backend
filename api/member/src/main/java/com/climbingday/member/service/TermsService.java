package com.climbingday.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.climbingday.domain.terms.repository.TermsRepository;
import com.climbingday.dto.terms.TermsDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsService {

	private final TermsRepository termsRepository;

	/**
	 * 이용약관 목록 조회
	 */
	public List<TermsDto> getTermsList() {
		return termsRepository.getAllTerms();
	}
}
