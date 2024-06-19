package com.climbingday.domain.center.repository;

import static com.climbingday.domain.center.QCenter.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.climbingday.dto.center.CenterDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CenterRepositoryImpl implements CenterCustom {
	private final JPAQueryFactory queryFactory;

	public Page<CenterDto> findAllCenter(Pageable pageable) {
		List<CenterDto> centerList = queryFactory
			.select(Projections.constructor(CenterDto.class,
				center.id,
				center.name,
				center.phoneNum,
				center.address,
				center.latitude,
				center.longitude,
				center.openTime,
				center.closeTime,
				center.description,
				center.notice,
				center.profileImage,
				center.member.nickName.as("memberNickName")
				))
			.from(center)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(queryFactory
			.select(center.count())
			.from(center)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(centerList, pageable, total);
	}
}
