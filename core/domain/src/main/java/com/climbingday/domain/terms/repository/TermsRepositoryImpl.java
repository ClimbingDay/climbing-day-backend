package com.climbingday.domain.terms.repository;

import static com.climbingday.domain.terms.QTerms.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.climbingday.dto.terms.TermsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<TermsDto> getAllTerms() {
		return queryFactory
			.select(Projections.constructor(TermsDto.class,
				terms.id,
				terms.type,
				terms.version,
				terms.contentUrl,
				terms.isMandatory,
				terms.createdDate
				))
			.from(terms)
			.fetch();
	}
}
