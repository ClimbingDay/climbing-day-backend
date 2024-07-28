package com.climbingday.domain.center;

import jakarta.persistence.Column;
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
public class CenterLevel {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "center_level_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "center_id")
	private Center center;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id")
	private Level level;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "color_id")
	private Color color;
}
