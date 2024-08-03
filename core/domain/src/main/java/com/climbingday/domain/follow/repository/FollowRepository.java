package com.climbingday.domain.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.follow.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustom {
}
