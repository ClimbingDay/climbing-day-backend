package com.climbingday.domain.terms.repository;

import static com.climbingday.domain.terms.QTerms.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.climbingday.dto.terms.TermsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsCustom {
	private final JPAQueryFactory queryFactory;

	/**
	 * 모든 이용약관 목록 조회
	 */
	@Override
	public List<TermsDto> getAllTerms() {
		return selectTermsQuery()
			.fetch();
	}

	/**
	 * 필수 이용약관 목록 조회
	 */
	@Override
	public List<TermsDto> getRequiredTerms() {
		return selectTermsQuery()
			.where(requiredTerms())
			.fetch();
	}

	private JPQLQuery<TermsDto> selectTermsQuery() {
		return queryFactory
			.select(Projections.constructor(TermsDto.class,
				terms.id,
				terms.type,
				terms.name,
				terms.version,
				terms.contentUrl,
				terms.isMandatory,
				terms.createdDate
			))
			.from(terms);
	}

	private BooleanExpression requiredTerms() {
		return terms.isMandatory.isTrue();
	}
}
