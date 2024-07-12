package com.climbingday.dto.center;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CenterLevelDto {
	private String name;

	private Map<Long, LevelColorDto> levelColor;
}