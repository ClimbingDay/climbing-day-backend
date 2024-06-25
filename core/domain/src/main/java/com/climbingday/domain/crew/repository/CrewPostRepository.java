package com.climbingday.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.crew.CrewPost;

public interface CrewPostRepository extends JpaRepository<CrewPost, Long>, CrewPostCustom {
}
