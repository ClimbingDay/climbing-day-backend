package com.climbingday.domain.center;

import java.time.LocalDateTime;

import com.climbingday.domain.member.Member;

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
public class Center {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "center_id")
	private Long id;

	private String centerNumber;		// 암장 전화번호

	private String address;				// 암장 주소

	private String latitude;			// 암장 위도

	private String longitude;			// 암장 경도

	private LocalDateTime openHour;		// 영업 시작 시간

	private LocalDateTime closeHour;	// 영업 종료 시간

	private String description; 		// 암장 소개

	private String notice;				// 암장 공지사항

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

}
