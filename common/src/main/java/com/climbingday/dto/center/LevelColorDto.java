package com.climbingday.dto.center;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelColorDto {
	private Long levelId;

	private String levelName;

	private String colorName;

	private String colorHex;
}
