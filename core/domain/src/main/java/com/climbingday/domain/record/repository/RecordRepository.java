package com.climbingday.domain.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.record.Record;

public interface RecordRepository extends JpaRepository<Record, Long>, RecordCustom {
}
