package com.climbingday.domain.memberCrew;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor @AllArgsConstructor
public class MemberCrewId implements Serializable {

	private Long memberId;

	private Long crewId;

	// equals and hashCode methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MemberCrewId that = (MemberCrewId) o;
		return Objects.equals(memberId, that.memberId) && Objects.equals(crewId, that.crewId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(memberId, crewId);
	}
}
