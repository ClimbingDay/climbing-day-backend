package com.climbingday.member.service;

import static com.climbingday.enums.CenterErrorCode.*;
import static com.climbingday.enums.EventErrorCode.*;
import static com.climbingday.enums.MemberErrorCode.*;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.center.Center;
import com.climbingday.domain.center.CenterLevel;
import com.climbingday.domain.center.repository.CenterLevelRepository;
import com.climbingday.domain.center.repository.CenterRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.domain.record.Record;
import com.climbingday.domain.record.RecordProblem;
import com.climbingday.domain.record.repository.RecordRepository;
import com.climbingday.dto.member.RecordRegisterDto;
import com.climbingday.dto.member.TryLevelDto;
import com.climbingday.member.exception.MemberException;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final CenterLevelRepository centerLevelRepository;
	private final RecordRepository recordRepository;

	@Transactional
	public Long registerRecord(UserDetailsImpl userDetails, RecordRegisterDto recordRegisterDto) {
		int tryListIndex = 0;

		// 회원 정보 검증
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

		// TO-DO 암장 색상, 레벨값을 가져와서 검증

		// 센터 정보 검증
		Center center = centerRepository.findById(recordRegisterDto.getCenterId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_CENTER));

		Record record = Record.fromRecordRegisterDto(recordRegisterDto, member);
		record.setCenter(center);

		// 센터 레벨 리스트 및 시도 레벨 리스트 가져오기
		List<CenterLevel> centerLevelList = centerLevelRepository.findAllByCenterIdOrderByLevelIdAsc(center.getId());
		List<TryLevelDto> tryList = recordRegisterDto.getTryLevelList();
		tryList.sort(Comparator.comparing(TryLevelDto::getTryLevelId));

		// 같은 레벨을 넘겨 받는 경우 처리 ??

		//레벨 매핑 및 검증
		for(CenterLevel centerLevel: centerLevelList) {
			if(tryListIndex >= tryList.size()) {
				break;
			}

			TryLevelDto tryLevelDto = tryList.get(tryListIndex);

			// 센터에 해당 레벨 존재
			if(centerLevel.getLevel().getId() == tryLevelDto.getTryLevelId()){
				record.getRecordProblemList().add(
					RecordProblem.fromCenterLevel(centerLevel, record, tryLevelDto.isSuccess())
				);
				tryListIndex++;
			}
		}

		if(tryListIndex != tryList.size()){
			throw new MemberException(NOT_EXISTS_CENTER_LEVEL);
		}

		return recordRepository.save(record).getId();
	}
}
