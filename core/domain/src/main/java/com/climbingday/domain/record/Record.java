package com.climbingday.domain.record;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
			.startTime(parseLocalDateTime(recordRegisterDto.getStartTime()))
			.endTime(parseLocalDateTime(recordRegisterDto.getEndTime()))
			.duration(recordRegisterDto.getDuration())
			.build();
	}

	private static LocalDateTime parseLocalDateTime(String dateTimeStr) {
		// 입력된 시간 문자열을 변환
		String[] parts = dateTimeStr.split(" ");
		if (parts.length == 2) {
			String datePart = parts[0];
			String timePart = parts[1];
			String[] timeParts = timePart.split(":");
			if (timeParts.length == 3) {
				timeParts[0] = timeParts[0].length() == 1 ? "0" + timeParts[0] : timeParts[0];
				timeParts[1] = timeParts[1].length() == 1 ? "0" + timeParts[1] : timeParts[1];
				timeParts[2] = timeParts[2].length() == 1 ? "0" + timeParts[2] : timeParts[2];
				dateTimeStr = datePart + " " + timeParts[0] + ":" + timeParts[1] + ":" + timeParts[2];
			}
		}

		// 변환된 문자열을 LocalDateTime으로 파싱
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		try {
			return LocalDateTime.parse(dateTimeStr, formatter);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("날짜 및 시간 형식이 잘못되었습니다: " + dateTimeStr, e);
		}
	}
}
