package com.climbingday.domain.center;

import com.climbingday.domain.MutableBaseEntity;
import com.climbingday.domain.member.Member;
import com.climbingday.dto.center.CenterRegisterDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Center extends MutableBaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "center_id")
	private Long id;

	private String name;						// 암장 이름

	private String phoneNum;					// 암장 전화번호

	private String address;						// 암장 주소

	private double latitude;					// 암장 위도

	private double longitude;					// 암장 경도

	private String openTime;					// 암장 시작 시간

	private String closeTime;					// 암장 종료 시간

	private String description; 				// 암장 소개

	private String notice;						// 암장 공지사항

	private String profileImage;				// 암장 이미지

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;						// 암장 관리자

	public static Center fromCenterRegisterDto(CenterRegisterDto centerRegisterDto, Member member) {

		return Center.builder()
			.name(centerRegisterDto.getName())
			.phoneNum(centerRegisterDto.getPhoneNum())
			.address(centerRegisterDto.getAddress())
			.latitude(centerRegisterDto.getLatitude())
			.longitude(centerRegisterDto.getLongitude())
			.openTime(centerRegisterDto.getOpenTime())
			.closeTime(centerRegisterDto.getCloseTime())
			.description(centerRegisterDto.getDescription())
			.notice(centerRegisterDto.getNotice())
			.member(member)
			.build();
	}
}
