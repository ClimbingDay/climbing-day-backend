package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.climbingday.domain.general.GeneralPost;
import com.climbingday.domain.general.repository.GeneralCommentRepository;
import com.climbingday.domain.general.repository.GeneralPostRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.general.GeneralCommentDto;
import com.climbingday.dto.general.GeneralPostDetailDto;
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
	private final GeneralCommentRepository generalCommentRepository;

	/**
	 * 일반 게시글 등록
	 */
	@Transactional
	public Long registerPost(GeneralPostRegDto generalPostRegDto, UserDetailsImpl userDetails) {

		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

		GeneralPost generalPost = GeneralPost.fromGeneralPostRegisterDto(generalPostRegDto, member);

		return generalPostRepository.save(generalPost).getId();
	}

	/**
	 * 모든 일반 게시글 조회
	 */
	@Transactional(readOnly = true)
	public Page<GeneralPostDto> getGeneralPosts(Pageable pageable) {
		return generalPostRepository.getAllPost(pageable);
	}

	/**
	 * 일반 게시글 상세 조회
	 */
	@Transactional(readOnly = true)
	public GeneralPostDetailDto getGeneralDetailPost(Long postId) {
		// 일반 게시글 조회
		GeneralPostDetailDto generalPostDto = generalPostRepository.getPost(postId)
			.orElseThrow(() -> new MemberException(NOT_EXISTS_POST));

		// 일반 게시글 댓글 조회
		generalPostDto.setComments(generalCommentRepository.getCommentsForPost(generalPostDto.getId()));

		return generalPostDto;
	}
}
