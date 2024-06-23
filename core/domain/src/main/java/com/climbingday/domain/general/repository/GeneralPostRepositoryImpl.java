package com.climbingday.domain.general.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GeneralPostRepositoryImpl implements GeneralPostCustom {

	private final JPAQueryFactory queryFactory;


}
