package com.climbingday.domain.record.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordCustom {
	private final JPAQueryFactory queryFactory;


}
