package com.climbingday.domain.sigungu.repository;

import static com.climbingday.domain.sigungu.QSigungu.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.climbingday.dto.sigungu.SigunguDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SigunguRepositoryImpl implements SigunguCustom {
	private final JPAQueryFactory queryFactory;

	/**
	 * 시군구 조회
	 */
	@Override
	public List<SigunguDto> getSigunguList(String name) {
		return queryFactory
			.select(Projections.constructor(SigunguDto.class,
				sigungu.code,
				sigungu.name
			))
			.from(sigungu)
			.where(sigungu.name.like("%" + name + "%"))
			.fetch();
	}
}
