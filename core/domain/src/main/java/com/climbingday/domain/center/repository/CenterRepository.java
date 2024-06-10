package com.climbingday.domain.center.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.center.Center;

public interface CenterRepository extends JpaRepository<Center, Long>, CenterCustom {
}
