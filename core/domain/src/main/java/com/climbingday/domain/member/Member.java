package com.climbingday.domain.member;

import java.sql.Date;

import com.climbingday.domain.MutableBaseEntity;
import com.climbingday.dto.member.MemberRegisterDto;
import com.climbingday.enums.member.EProviders;
import com.climbingday.enums.member.ERoles;
import com.climbingday.enums.member.EStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member extends MutableBaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String email;						// 아이디(이메일)

	@Setter
	private String password;					// 비밀번호

	private String nickName;					// 닉네임

	private Date birthDate;						// 생년월일

	private String phoneNumber;					// 전화번호

	@Setter
	private String profileImage;				// 프로필 이미지

	private String introduce;					// 소개

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private EStatus status = EStatus.ACTIVE;	// 상태

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ERoles roles = ERoles.ROLE_USER;	// 역할(권한)

	@Enumerated(EnumType.STRING)
	private EProviders provider;				// 제공자

	private String sigunguCode;					// 시군구 코드

	private double latitude;					// 위도

	private double longitude;					// 경도

	@Setter
	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private MemberSettings memberSettings;

	public static Member fromMemberRegisterDto(MemberRegisterDto registerDto) {
		String introduce = "";

		if(registerDto.getIntroduce() != null && !registerDto.getIntroduce().isEmpty())
			introduce = registerDto.getIntroduce();

		Member member = Member.builder()
			.email(registerDto.getEmail())
			.nickName(registerDto.getNickName())
			.phoneNumber(String.join("-", registerDto.getPhoneNumber()))
			.birthDate(Date.valueOf(registerDto.getBirthDate()))
			.profileImage("https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg")
			.introduce(introduce)
			.provider(registerDto.getProvider())
			.build();

		MemberSettings settings = MemberSettings.builder()
			.member(member)
			.build();

		member.setMemberSettings(settings);

		return member;
	}
}
