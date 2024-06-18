package com.climbingday.dto.center;

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

	private String openTime;

	private String closeTime;

	private String description;

	private String notice;

	private String memberNickName;
}
