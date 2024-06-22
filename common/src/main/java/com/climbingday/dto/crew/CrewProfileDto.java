package com.climbingday.dto.crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CrewProfileDto {
	private long id;

	private String name;

	private String profileImage;
}
