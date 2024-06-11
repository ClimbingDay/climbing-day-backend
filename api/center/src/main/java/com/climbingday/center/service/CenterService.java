package com.climbingday.center.service;

import static com.climbingday.enums.CenterErrorCode.*;
import static com.climbingday.enums.MemberErrorCode.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.center.exception.CenterException;
import com.climbingday.domain.center.Center;
import com.climbingday.domain.center.repository.CenterRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.center.CenterDto;
import com.climbingday.dto.center.CenterRegisterDto;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CenterService {
	private final CenterRepository centerRepository;
	private final MemberRepository memberRepository;

	/**
	 * 암장 등록
	 */
	@Transactional
	public Long registerCenter(CenterRegisterDto centerRegisterDto, UserDetailsImpl userDetails) {
		// 암장 중복 확인
		duplicateCenter(centerRegisterDto.getName());

		// 회원 확인
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new CenterException(EXISTS_NOT_MEMBER));

		Center center = Center.fromCenterRegisterDto(centerRegisterDto, member);

		return centerRepository.save(center).getId();
	}

	/**
	 * 암장 조회
	 */
	@Transactional(readOnly = true)
	public Page<CenterDto> getCenterPage(Pageable pageable) {
		return centerRepository.findAllCenter(pageable);
	}

	private void duplicateCenter(String centerName) {
		if(centerRepository.existsByName(centerName)) {
			throw new CenterException(DUPLICATED_CENTER_NAME);
		}
	}
}
