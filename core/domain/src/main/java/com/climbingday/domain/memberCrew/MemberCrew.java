package com.climbingday.domain.memberCrew;

import com.climbingday.domain.crew.Crew;
import com.climbingday.domain.member.Member;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberCrew {
	@EmbeddedId
	private MemberCrewId id;

	@ManyToOne
	@MapsId("memberId")
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@MapsId("crewId")
	@JoinColumn(name = "crew_id")
	private Crew crew;

	public MemberCrew(Member member, Crew crew) {
		this.member = member;
		this.crew = crew;
		this.id = new MemberCrewId(member.getId(), crew.getId());
	}
}
