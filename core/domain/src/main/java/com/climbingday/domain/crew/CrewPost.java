package com.climbingday.domain.crew;

import com.climbingday.domain.MutableBaseEntity;
import com.climbingday.domain.member.Member;
import com.climbingday.dto.crew.CrewPostRegDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class CrewPost extends MutableBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crew_post_id")
	private Long id;							// 크루 게시글 id

	private String title;						// 크루 게시글 제목

	@Lob
	private String content;						// 크루 게시글 내용

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public static CrewPost fromCrewPostRegisterDto(CrewPostRegDto crewPostRegDto, Member member) {
		return CrewPost.builder()
			.title(crewPostRegDto.getTitle())
			.content(crewPostRegDto.getContent())
			.member(member)
			.build();
	}
}