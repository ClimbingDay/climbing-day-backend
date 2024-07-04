package com.climbingday.dto.terms;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsDto {
	private Long id;

	private String type;

	private String version;

	private String contentUrl;

	@JsonProperty("isMandatory")
	private boolean isMandatory;

	private LocalDateTime createdDate;
}
