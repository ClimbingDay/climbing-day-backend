package com.climbingday.dto.center;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class CenterDto {
	private long id;

	private String name;

	private String phoneNum;

	private String address;

	private double latitude;

	private double longitude;

	private LocalTime openTime;

	private LocalTime closeTime;

	private String description;

	private String notice;

	private String memberNickName;
}
