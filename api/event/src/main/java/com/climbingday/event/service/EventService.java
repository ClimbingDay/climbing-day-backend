package com.climbingday.event.service;

import static com.climbingday.enums.EventErrorCode.*;
import static com.climbingday.enums.MemberErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.member.MemberLocationDto;
import com.climbingday.dto.member.MemberMatchDto;
import com.climbingday.event.exception.EventException;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
	private final MemberRepository memberRepository;

	/**
	 * 주변 회원 찾기(매칭)
	 */
	public List<MemberMatchDto> memberMatch(MemberLocationDto memberLocationDto, UserDetailsImpl userDetail) {
		double initDistance = 5; // 초기 거리(5km)
		int maxLength = 3; // 최대 반복 횟수
		int limit = 4;

		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetail.getId())
			.orElseThrow(() -> new EventException(NOT_EXISTS_MEMBER));

		// 종목 필터링 처리 해야함

		// 팔로워 많은 사람 1명 선택 처리

		List<MemberMatchDto> nearbyMemberList = new ArrayList<>();

		for (int i = 0; i < maxLength; i++) {
			// 내 위치에서 가까운 사람 4명 랜덤 선택
			nearbyMemberList = memberRepository.getMemberByDistance(memberLocationDto.getLatitude(),
				memberLocationDto.getLongitude(), initDistance, limit, member.getId());

			if (nearbyMemberList.size() >= limit) {
				break;
			}

			// 거리 증가
			initDistance += 5;
		}

		// 결과 리스트에 추가

		// 팔로워 리스트가 추가되면 + 1처리 해야함
		if (nearbyMemberList.size() < 1) {
			throw new EventException(NOT_FIND_MATCHING_MEMBER);
		}

		return nearbyMemberList;
	}
}
