package com.climbingday.domain.follow;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.climbingday.domain.member.Member;

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
import lombok.NoArgsConstructor;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
public class Follow  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id")
	private Member follower;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id")
	private Member following;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;
}
