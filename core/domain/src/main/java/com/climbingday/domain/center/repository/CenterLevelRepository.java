package com.climbingday.domain.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.center.CenterLevel;

public interface CenterLevelRepository extends JpaRepository<CenterLevel, Long>, CenterLevelCustom {
	List<CenterLevel> findAllByCenterIdOrderByLevelIdAsc(Long centerId);
}
