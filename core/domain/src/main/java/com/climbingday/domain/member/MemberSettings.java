package com.climbingday.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberSettings {
	@Id
	@Column(name = "member_id")
	private Long memberId;

	@Builder.Default
	private Boolean isProfileVisible = true;

	@Builder.Default
	private Boolean isMatchingEnabled = true;

	@OneToOne
	@MapsId
	@JoinColumn(name = "member_id")
	private Member member;
}
