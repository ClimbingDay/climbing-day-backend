package com.climbingday.domain.center.repository;

import static com.climbingday.domain.center.QCenter.*;
import static com.climbingday.domain.center.QCenterLevel.*;
import static com.climbingday.domain.center.QColor.*;
import static com.climbingday.domain.center.QLevel.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CenterLevelRepositoryImpl implements CenterLevelCustom {
	private final JPAQueryFactory queryFactory;

	public List<Tuple> getCenterLevels(Long centerId) {
		return queryFactory.select(
				center.name,
				color.name,
				color.hexValue,
				level.name,
				level.id
			)
			.from(centerLevel)
			.join(centerLevel.center, center)
			.join(centerLevel.level, level)
			.join(centerLevel.color, color)
			.fetch();
	}
}
