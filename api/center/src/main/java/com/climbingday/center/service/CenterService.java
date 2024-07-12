package com.climbingday.center.service;

import static com.climbingday.enums.CenterErrorCode.*;
import static com.climbingday.enums.GlobalErrorCode.*;
import static com.climbingday.enums.MemberErrorCode.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.climbingday.center.exception.CenterException;
import com.climbingday.domain.center.Center;
import com.climbingday.domain.center.repository.CenterLevelRepository;
import com.climbingday.domain.center.repository.CenterRepository;
import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.dto.center.CenterDto;
import com.climbingday.dto.center.CenterLevelDto;
import com.climbingday.dto.center.CenterRegisterDto;
import com.climbingday.dto.center.LevelColorDto;
import com.climbingday.security.service.UserDetailsImpl;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CenterService {

	@Value("${s3.bucket.name}")
	private String bucketName;

	private final CenterRepository centerRepository;
	private final MemberRepository memberRepository;
	private final CenterLevelRepository centerLevelRepository;
	private final AmazonS3 s3Client;

	private final String DEFAULT_PROFILE_IMAGE = "https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg";

	/**
	 * 암장 등록
	 */
	@Transactional
	public Long registerCenter(CenterRegisterDto centerRegisterDto, MultipartFile file, UserDetailsImpl userDetails) {
		// 암장 중복 확인
		duplicateCenter(centerRegisterDto.getName());

		// 회원 정보 가져오기
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new CenterException(NOT_EXISTS_MEMBER));

		Center center = Center.fromCenterRegisterDto(centerRegisterDto, member);

		if (!(file == null || file.isEmpty())) {
			center.setProfileImage(uploadFile(file));
		}else {
			center.setProfileImage(DEFAULT_PROFILE_IMAGE);
		}

		return centerRepository.save(center).getId();
	}

	/**
	 * 모든 암장 조회(페이징)
	 */
	@Transactional(readOnly = true)
	public Page<CenterDto> getCenterPage(Pageable pageable) {
		return centerRepository.findAllCenter(pageable);
	}

	/**
	 * 암장 조회(이름)
	 */
	@Transactional(readOnly = true)
	public List<CenterDto> getCenter(String centerName) {
		List<CenterDto> centers = centerRepository.getCenterByName(centerName);
		if(!centers.isEmpty()) {
			return centers;
		}else {
			throw new CenterException(EXISTS_NOT_CENTER);
		}
	}

	private void duplicateCenter(String centerName) {
		if (centerRepository.existsByName(centerName)) {
			throw new CenterException(DUPLICATED_CENTER_NAME);
		}
	}

	/**
	 * 암장 레벨 및 색상 조회
	 */
	@Transactional(readOnly = true)
	public CenterLevelDto getCenterLevel(Long centerId) {
		// 암장 조회
		Center center = centerRepository.findCenterById(centerId)
				.orElseThrow(() -> new CenterException(EXISTS_NOT_CENTER));

		List<Tuple> centerLevels = centerLevelRepository.getCenterLevels(center.getId());

		CenterLevelDto response = new CenterLevelDto();
		if(!centerLevels.isEmpty()) {
			response.setName(centerLevels.get(0).get(0, String.class));
			Map<Long, LevelColorDto> levelColorDtoMap = new HashMap<>();

			for(Tuple centerLevel: centerLevels) {
				LevelColorDto levelColorDto = LevelColorDto.builder()
					.levelName(centerLevel.get(3, String.class))
					.colorName(centerLevel.get(1, String.class))
					.colorHex(centerLevel.get(2, String.class))
					.build();

				levelColorDtoMap.put(centerLevel.get(4, Long.class), levelColorDto);
			}

			response.setLevelColor(levelColorDtoMap);
		}

		return response;
	}

	/**
	 * s3 이미지 업로드
	 */
	public String uploadFile(MultipartFile file) {
		String fileKey = System.currentTimeMillis() + "_" + file.getOriginalFilename();

		try {
			File fileObj = convertMultiPartFileToFile(file);
			s3Client.putObject(new PutObjectRequest(bucketName, fileKey, fileObj));
			fileObj.delete(); // 임시 파일 삭제

			// 엔드 포인트 Url
			return s3Client.getUrl(bucketName, fileKey).toString();

		} catch (IOException e) {
			e.printStackTrace();
			throw new CenterException(S3_UPLOAD_FAILED);
		}
	}

	private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();
		return convertedFile;
	}
}
