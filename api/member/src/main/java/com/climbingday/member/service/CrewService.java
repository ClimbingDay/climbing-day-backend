package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.crew.CrewPost;
import com.climbingday.domain.crew.repository.CrewPostRepository;
import com.climbingday.domain.crew.repository.CrewRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.crew.CrewPostDto;
import com.climbingday.dto.crew.CrewPostRegDto;
import com.climbingday.dto.crew.CrewProfileDto;
import com.climbingday.member.exception.MemberException;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewService {

	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;
	private final CrewPostRepository crewPostRepository;

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

	/**
	 * 크루 게시글 등록
	 */
	@Transactional
	public Long registerPost(CrewPostRegDto crewPostRegDto, UserDetailsImpl userDetails) {
		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

		CrewPost crewPost = CrewPost.fromCrewPostRegisterDto(crewPostRegDto, member);

		return crewPostRepository.save(crewPost).getId();
	}

	/**
	 * 모든 크루 게시글 조회
	 */
	@Transactional(readOnly = true)
	public Page<CrewPostDto> getCrewPosts(Pageable pageable) {
		return crewPostRepository.getAllPost(pageable);
	}
}
