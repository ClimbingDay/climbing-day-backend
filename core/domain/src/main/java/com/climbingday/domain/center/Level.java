package com.climbingday.domain.center;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Level {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "level_id")
	private Long id;

	private String name;
}
