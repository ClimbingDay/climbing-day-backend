package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.domain.record.Record;
import com.climbingday.domain.record.repository.RecordRepository;
import com.climbingday.dto.member.RecordRegisterDto;
import com.climbingday.member.exception.MemberException;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
	private final MemberRepository memberRepository;
	private final RecordRepository recordRepository;

	@Transactional
	public Long registerRecord(UserDetailsImpl userDetails, RecordRegisterDto recordRegisterDto) {
		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

		Record record = Record.fromRecordRegisterDto(recordRegisterDto, member);

		return recordRepository.save(record).getId();
	}
}
