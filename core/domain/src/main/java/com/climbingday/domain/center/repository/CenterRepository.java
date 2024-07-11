package com.climbingday.domain.center.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.center.Center;

public interface CenterRepository extends JpaRepository<Center, Long>, CenterCustom {
	boolean existsByName(String name);

	Optional<Center> findCenterByName(String name);
}
