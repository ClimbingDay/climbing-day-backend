package com.climbingday.dto.center;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CenterLevelDto {
	private String name;

	@Builder.Default
	private List<LevelColorDto> levelColor = new ArrayList<>();
}
