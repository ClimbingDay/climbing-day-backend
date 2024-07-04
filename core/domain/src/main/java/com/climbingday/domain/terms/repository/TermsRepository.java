package com.climbingday.domain.terms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.record.Record;

public interface TermsRepository extends JpaRepository<Record, Long>, TermsCustom {
}
