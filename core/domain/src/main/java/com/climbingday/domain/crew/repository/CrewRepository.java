package com.climbingday.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.crew.Crew;

public interface CrewRepository extends JpaRepository<Crew, Long>, CrewCustom {

}
