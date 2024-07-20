package com.climbingday.member.service;

import static com.climbingday.enums.MemberErrorCode.*;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.dto.member.MemberTokenDto;
import com.climbingday.dto.member.NaverResponseDto;
import com.climbingday.dto.member.NaverUserInfoDto;
import com.climbingday.dto.member.OAuthLoginDto;
import com.climbingday.enums.member.EProviders;
import com.climbingday.member.exception.MemberException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {
	private final MemberService memberService;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public MemberTokenDto login(OAuthLoginDto oAuthLoginDto) {
		try {
			NaverUserInfoDto userInfo = getNaverUserInfo(oAuthLoginDto.getAccessToken());
			Optional<Member> member = memberRepository.findByEmailAndProvider(userInfo.getNaverResponseDto().getEmail(),
				EProviders.NAVER);

			if (member.isPresent()) {
				// 기존 회원 로그인 처리
				return memberService.oAuthLogin(member.get());
			} else {
				throw new MemberException(NOT_EXISTS_MEMBER);
			}
		} catch (Exception e) {
			throw new MemberException(NAVER_LOGIN_ERROR);
		}
	}

	@Override
	@Transactional
	public MemberTokenDto registerAndLogin(OAuthLoginDto oAuthLoginDto) {
		try {
			NaverUserInfoDto userInfo = getNaverUserInfo(oAuthLoginDto.getAccessToken());
			Optional<Member> optionalMember = memberRepository.findByEmail(userInfo.getNaverResponseDto().getEmail());

			if (optionalMember.isPresent()) {
				Member member = optionalMember.get();

				// 클라이밍 데이 로그인 회원
				if(member.getProvider() == null) {
					throw new MemberException(CLIMBING_DAY_LOGIN_MEMBER);
				}

				// 기존 회원 로그인 처리
				return memberService.oAuthLogin(member);
			} else {
				// 신규 가입 및 로그인 처리
				MemberRegisterDto memberRegisterDto = convertToRegisterDto(userInfo, oAuthLoginDto);

				// nickName 랜덤생성 -> to do 최적화 필요
				while(memberRepository.existsByNickName(memberRegisterDto.getNickName())) {
					// 새로운 랜덤 닉네임 가져오기
					memberRegisterDto.setNickName(oAuthLoginDto.generateRandomNickName());
				}

				Member registeredMember = memberService.registerMember(memberRegisterDto);
				return memberService.oAuthLogin(registeredMember);
			}
		} catch (
			Exception e) {
			throw new MemberException(NAVER_LOGIN_ERROR);
		}
	}

	/**
	 * Naver 유저 정보 조회
	 */
	private NaverUserInfoDto getNaverUserInfo(String token) {
		WebClient webClient = WebClient.builder()
			.baseUrl("https://openapi.naver.com/v1/nid/me")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.build();

		return webClient.get()
			.retrieve()
			.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
				response -> response.bodyToMono(NaverUserInfoDto.class)
					.flatMap(errorBody -> {
						HttpStatus status = (HttpStatus)response.statusCode();
						return Mono.error(WebClientResponseException.create(
							status.value(),
							status.getReasonPhrase(),
							response.headers().asHttpHeaders(),
							null,
							null
						));
					})
			)
			.bodyToMono(NaverUserInfoDto.class)
			.block();
	}

	private MemberRegisterDto convertToRegisterDto(NaverUserInfoDto userInfo, OAuthLoginDto oAuthLoginDto) {
		NaverResponseDto naverUserInfo = userInfo.getNaverResponseDto();
		String password = UUID.randomUUID().toString().replace("-", "");

		return MemberRegisterDto.builder()
			.email(naverUserInfo.getEmail())
			.nickName(oAuthLoginDto.getNickName())
			.password(password)
			.passwordConfirm(password)
			.phoneNumber(naverUserInfo.getPhoneNumber())
			.birthDate(naverUserInfo.getBirthyear() + "-" + naverUserInfo.getBirthday())
			.terms(oAuthLoginDto.getTerms())
			.provider(EProviders.NAVER)
			.build();
	}
}
