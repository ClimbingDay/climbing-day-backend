package com.climbingday.member.service;

import static com.climbingday.enums.EventErrorCode.*;
import static com.climbingday.enums.GlobalErrorCode.*;
import static com.climbingday.enums.MemberErrorCode.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.climbingday.domain.common.repository.RedisRepository;
import com.climbingday.domain.common.repository.S3Repository;
import com.climbingday.domain.follow.repository.FollowRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.MemberTerms;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.domain.member.repository.MemberTermsRepository;
import com.climbingday.domain.terms.Terms;
import com.climbingday.domain.terms.repository.TermsRepository;
import com.climbingday.dto.crew.CrewProfileDto;
import com.climbingday.dto.member.EmailAuthDto;
import com.climbingday.dto.member.EmailDto;
import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberLoginDto;
import com.climbingday.dto.member.MemberMyProfileDto;
import com.climbingday.dto.member.MemberProfileDto;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.dto.member.MemberTokenDto;
import com.climbingday.dto.member.PasswordResetDto;
import com.climbingday.dto.terms.TermsDto;
import com.climbingday.dto.terms.TermsListDto;
import com.climbingday.member.exception.MemberException;
import com.climbingday.security.jwt.JwtProvider;
import com.climbingday.security.service.UserDetailsImpl;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final TermsRepository termsRepository;
	private final MemberTermsRepository memberTermsRepository;
	private final FollowRepository followRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RedisRepository redisRepository;
	private final S3Repository s3Repository;
	private final RestTemplate restTemplate;

	private final String DEFAULT_PROFILE_IMAGE = "https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg";

	@Value("${service-url}")
	private String serviceUrl;

	/**
	 * 회원 등록
	 */
	@Transactional
	public Member registerMember(MemberRegisterDto memberRegisterDto) {
		checkEmail(memberRegisterDto.getEmail());														// 이메일 중복 확인
		validatePassword(memberRegisterDto.getPassword(), memberRegisterDto.getPasswordConfirm());		// 비밀번호 일치 확인
		checkPhoneNumber(memberRegisterDto.getPhoneNumber());											// 휴대폰 중복 확인
		checkNickName(memberRegisterDto.getNickName());													// 닉네임 중복 확인
		checkRequiredTerms(memberRegisterDto.getTerms());												// 필수 이용약관 확인

		Member member = Member.fromMemberRegisterDto(memberRegisterDto);
		member.setPassword(passwordEncoder.encode(memberRegisterDto.getPassword()));

		Map<String, Boolean> termsMap = memberRegisterDto.getTerms().getFieldnamesAndValues();

		Member createMember = memberRepository.save(member);

		// 약관 조회 및 회원 약관 동의 내용 저장
		for(String type: termsMap.keySet()) {
			Terms terms = termsRepository.findByType(type)
				.orElseThrow(() -> new MemberException(NOT_FIND_TERMS));

			Boolean isAgree = termsMap.get(type);

			MemberTerms memberTerms = MemberTerms.builder()
				.member(createMember)
				.terms(terms)
				.isAgree(isAgree)
				.agreeDate(isAgree ? LocalDateTime.now() : null)
				.build();

			memberTermsRepository.save(memberTerms);
		}

		return createMember;
	}

	/**
	 * 회원 로그인
	 */
	public MemberTokenDto login(MemberLoginDto memberLogindto) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			memberLogindto.getEmail(),
			memberLogindto.getPassword()
		);

		Authentication authenticated = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetail = (UserDetailsImpl) authenticated.getPrincipal();

		MemberTokenDto tokenDto = createToken(userDetail);
		registerRedisRefreshToken(userDetail, tokenDto);

		return tokenDto;
	}

	/**
	 * 소셜 로그인
	 */
	public MemberTokenDto oAuthLogin(Member member) {
		UserDetailsImpl userDetail = (UserDetailsImpl) UserDetailsImpl.from(member);

		MemberTokenDto tokenDto = createToken(userDetail);
		registerRedisRefreshToken(userDetail, tokenDto);

		return tokenDto;
	}

	/**
	 * 비밀번호 재설정
	 */
	public void passwordReset(PasswordResetDto passwordResetDto) {
		String email = passwordResetDto.getEmail();

		// 회원 체크
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

		if(emailAuthCheck(email)) {
			validatePassword(passwordResetDto.getPassword(), passwordResetDto.getPasswordConfirm());

			member.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
			memberRepository.save(member);

			redisRepository.deleteRedisInfo(email);
		}
	}

	/**
	 * 이메일 인증 여부 체크
	 */
	public boolean emailAuthCheck(String email) {
		Map emailVerificationInfo = redisRepository.getEmailCodeAndConfirm(email);

		String confirm = (String)emailVerificationInfo.get("confirm");

		if(confirm == null || !confirm.equals("Y")) {
			throw new MemberException(NOT_AUTHENTICATED_EMAIL);
		}

		return true;
	}

	/**
	 * 이메일 인증 코드 요청
	 */
	public void emailAuth(EmailDto emailDto) {
		String email = emailDto.getEmail();
		String authCode = generateCode();
		sendEmailVerification(email, authCode);
	}

	/**
	 * 이메일 인증 코드 확인 및 인증 상태 변경(Confirm 값 변경)
	 */
	public void emailAuthConfirm(EmailAuthDto emailAuthDto) {

		Map redisEmailInfo = redisRepository.getEmailCodeAndConfirm(emailAuthDto.getEmail());

		Optional.ofNullable(redisEmailInfo.get("authCode"))
			.ifPresentOrElse(
				authCode -> {
					if(emailAuthDto.getAuthCode().equals(authCode)) {
						redisRepository.setEmailCodeAndConfirm(emailAuthDto.getEmail(), (String)authCode, "Y");
					}else {
						throw new MemberException(NOT_MATCHED_AUTH_CODE);
					}
				},
				() -> {
					throw new MemberException(NOT_MATCHED_AUTH_CODE);
				}
			);
	}

	/**
	 * 회원 조회
	 */
	@Transactional(readOnly = true)
	public List<MemberDto> getAllMember() {
		return memberRepository.getAllMember();
	}

	/**
	 * 토큰 재발급
	 */
	public MemberTokenDto getNewAccessToken(UserDetailsImpl userDetail, String headerRefreshToken) {
		// 정상적으로 로그인 했는지를 판별하기 위해서 redis에 있는 refreshToken과 비교를 한다.
		Map redisRefreshToken = redisRepository.getRefreshToken(userDetail.getId());

		headerRefreshToken = headerRefreshToken.substring("Bearer ".length());

		if(headerRefreshToken.equals(String.valueOf(redisRefreshToken.get("refreshToken")))){
			MemberTokenDto tokenDto = createToken(userDetail);
			int refreshExpirationSeconds = jwtProvider.getRefreshExpirationSeconds();
			redisRepository.setRedisRefreshToken(userDetail.getId(), tokenDto.getRefreshToken(), refreshExpirationSeconds);
			return tokenDto;
		}else {
			throw new MemberException(VALIDATION_TOKEN_FAILED);
		}
	}

	/**
	 * email 중복 체크
	 */
	public void checkEmail(String email) {
		if(memberRepository.existsByEmail(email)){
			throw new MemberException(DUPLICATED_MEMBER_EMAIL);
		}
	}

	/**
	 * password, passwordConfirm 일치 여부 체크
	 */
	private void validatePassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(NOT_MATCHED_PASSWORD);
		}
	}

	/**
	 * phoneNumber 중복 체크
	 */
	public void checkPhoneNumber(String phoneNumber) {
		if(memberRepository.existsByPhoneNumber(phoneNumber)){
			throw new MemberException(DUPLICATED_MEMBER_PHONE_NUMBER);
		}
	}

	/**
	 * nickName 중복 체크
	 */
	public void checkNickName(String nickName) {
		if(memberRepository.existsByNickName(nickName)) {
			throw new MemberException(DUPLICATED_MEMBER_NICK_NAME);
		}
	}

	/**
	 * 내 프로필 조회
	 */
	public MemberMyProfileDto getMyPage(UserDetailsImpl userDetails) {
		List<Tuple> myProfiles = memberRepository.getMyPage(userDetails.getId());
		Tuple myProfileTuple = myProfiles.get(0);

		MemberMyProfileDto response = MemberMyProfileDto.builder()
			.id(myProfileTuple.get(0, Long.class))
			.nickName(myProfileTuple.get(1, String.class))
			.profileImage(myProfileTuple.get(2, String.class))
			.introduce(myProfileTuple.get(3, String.class))
			.build();

		// 크루 처리
		for(Tuple myProfile: myProfiles) {
			if(myProfile.get(4, Long.class) != null) {
				CrewProfileDto crewProfileDto = CrewProfileDto.builder()
					.id(myProfile.get(4, Long.class))
					.name(myProfile.get(5, String.class))
					.profileImage(myProfile.get(6, String.class))
					.build();

				response.getCrew().add(crewProfileDto);
			}
		}

		// 팔로워 수
		response.setFollowerCount(followRepository.followerCount(response.getId()));
		// 팔로잉 수
		response.setFollowingCount(followRepository.followingCount(response.getId()));

		return response;
	}

	/**
	 * 이메일 인증 코드 생성
	 */
	private String generateCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 숫자 생성
		return String.valueOf(code);
	}

	/**
	 * 이메일 서버에 이메일 인증 코드 요청
	 */
	public void sendEmailVerification(String email, String authCode) {
		String url = serviceUrl + "/v1/mail/verification/send";

		try{
			Map<String, String> emailInfo = new HashMap<>();
			emailInfo.put("email", email);
			emailInfo.put("authCode", authCode);

			ResponseEntity<String> response = restTemplate.postForEntity(url, emailInfo, String.class);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new MemberException(UNABLE_TO_SEND_EMAIL);
			}

			redisRepository.setEmailCodeAndConfirm(email, authCode, "N");
		}catch (RestClientException e) {
			throw new MemberException(UNABLE_TO_SEND_EMAIL);
		}
	}

	/**
	 * 내 프로필 수정
	 */
	@Transactional
	public void updateProfile(
		UserDetailsImpl userDetail,
		MemberProfileDto memberProfileDto,
		MultipartFile file
	) {
		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetail.getId())
			.orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

		if(memberProfileDto != null) {
			Map<String, String> nonNullFields = memberProfileDto.getNonNullFields();

			// 닉네임 변경을 해야하는 경우 중복체크
			if(nonNullFields.containsKey("nickName"))
				checkNickName(nonNullFields.get("nickName"));

			// 회원 프로필 이미지를 제외한 정보 수정
			nonNullFields.forEach((fieldName, value) -> {
				try {
					Field field = Member.class.getDeclaredField(fieldName);
					field.setAccessible(true);
					field.set(member, value);
				}catch (NoSuchFieldException | IllegalAccessException e) {
					if(e.getMessage().equals(NOT_MATCHED_PASSWORD.getErrorMessage())){
						throw new MemberException(NOT_MATCHED_PASSWORD);
					}else {
						throw new MemberException(INTERNAL_SERVER_ERROR);
					}
				}
			});
		}

		// 회원 프로필 이미지 수정
		if(!(file==null || file.isEmpty())) {
			try {
				String profileImageUrl = s3Repository.uploadFile(file);
				member.setProfileImage(profileImageUrl);
			}catch(IOException e) {
				throw new MemberException(S3_UPLOAD_FAILED);
			}
		}

		memberRepository.save(member);
	}

	/**
	 * 토큰 생성(Access Token, Refresh Token)
	 */
	private MemberTokenDto createToken(UserDetailsImpl userDetail) {
		String accessToken = jwtProvider.createAccessToken(userDetail);
		String refreshToken = jwtProvider.createRefreshToken(userDetail);

		return MemberTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	/**
	 * 필수 이용약관 확인
	 */
	private void checkRequiredTerms(TermsListDto termsListDto) {
		List<TermsDto> requiredTerms = termsRepository.getRequiredTerms();

		for(TermsDto terms: requiredTerms) {
			// 필수 약관인데 동의하지 않은 경우
			if(terms.isMandatory() && !termsListDto.getTerm(terms.getType()))
				throw new MemberException(DISAGREE_REQUIRED_TERMS);
		}
	}

	/**
	 * refreshToken redis 저장
	 */
	private void registerRedisRefreshToken(UserDetailsImpl userDetail, MemberTokenDto tokenDto) {
		int refreshExpirationSeconds = jwtProvider.getRefreshExpirationSeconds();
		redisRepository.setRedisRefreshToken(userDetail.getId(), tokenDto.getRefreshToken(), refreshExpirationSeconds);
	}

}
