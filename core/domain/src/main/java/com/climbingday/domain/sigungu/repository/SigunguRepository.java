package com.climbingday.domain.sigungu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.sigungu.Sigungu;

public interface SigunguRepository extends JpaRepository<Sigungu, String>, SigunguCustom {
}
