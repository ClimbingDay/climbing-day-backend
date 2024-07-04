package com.climbingday.domain.terms.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsCustom {
	private final JPAQueryFactory queryFactory;


}
