package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.general.GeneralPost;
import com.climbingday.domain.general.repository.GeneralPostRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.general.GeneralPostDto;
import com.climbingday.dto.general.GeneralPostRegDto;
import com.climbingday.member.exception.MemberException;
import com.climbingday.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralService {

	private final MemberRepository memberRepository;
	private final GeneralPostRepository generalPostRepository;

	@Transactional
	public Long registerPost(GeneralPostRegDto generalPostRegDto, UserDetailsImpl userDetails) {

		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new MemberException(EXISTS_NOT_MEMBER));

		GeneralPost generalPost = GeneralPost.fromGeneralPostRegisterDto(generalPostRegDto, member);

		return generalPostRepository.save(generalPost).getId();
	}

	@Transactional(readOnly = true)
	public Page<GeneralPostDto> getGeneralPosts(Pageable pageable) {
		return generalPostRepository.getAllPost(pageable);
	}
}
