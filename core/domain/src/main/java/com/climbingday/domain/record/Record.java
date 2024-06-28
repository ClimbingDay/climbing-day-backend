package com.climbingday.domain.record;

import java.time.LocalDateTime;

import com.climbingday.domain.member.Member;
import com.climbingday.dto.record.RecordRegisterDto;

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
public class Record {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Long duration;

	public static Record fromRecordRegisterDto(RecordRegisterDto recordRegisterDto, Member member) {
		return Record.builder()
			.member(member)
			.startTime(LocalDateTime.parse(recordRegisterDto.getStartTime()))
			.endTime(LocalDateTime.parse(recordRegisterDto.getEndTime()))
			.duration(recordRegisterDto.getDuration())
			.build();
	}
}
