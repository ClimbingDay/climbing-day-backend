package com.climbingday.domain.center.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.center.CenterLevel;

public interface CenterLevelRepository extends JpaRepository<CenterLevel, Long>, CenterLevelCustom {

}
