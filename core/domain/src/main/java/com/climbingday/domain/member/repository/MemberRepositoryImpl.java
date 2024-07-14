package com.climbingday.domain.member.repository;

import static com.climbingday.domain.crew.QCrew.*;
import static com.climbingday.domain.member.QMember.*;
import static com.climbingday.domain.memberCrew.QMemberCrew.*;
import static com.climbingday.domain.sigungu.QSigungu.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.climbingday.dto.member.MemberDto;
import com.climbingday.dto.member.MemberMatchDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<MemberDto> getAllMember() {
		return queryFactory
			.select(Projections.constructor(MemberDto.class,
				member.id,
				member.email,
				member.status,
				member.roles
			))
			.from(member)
			.fetch();
	}

	@Override
	public List<Tuple> getMyPage(Long id) {
		return queryFactory
			.select(
				member.id,
				member.nickName,
				member.profileImage,
				member.introduce,
				crew.id,
				crew.name,
				crew.profileImage
			)
			.from(member)
			.leftJoin(memberCrew).on(member.id.eq(memberCrew.member.id))
			.leftJoin(crew).on(memberCrew.crew.id.eq(crew.id))
			.where(memberId(id))
			.fetch();
	}

	@Override
	public List<MemberMatchDto> getMemberByDistance(double myLat, double myLon, double distance, int limit,
		Long memberId) {
		// 지구 반지름(킬로미터 단위)
		double radius = 6371;

		// Haversine 공식 사용: 두 지점 간의 거리를 구하는 공식
		NumberExpression<Double> haversine = Expressions.numberTemplate(Double.class,
			"{0} * acos(cos(radians({1})) * cos(radians({2})) * cos(radians({3}) - radians({4})) + sin(radians({1})) * sin(radians({2})))",
			radius, myLat, member.latitude, member.longitude, myLon);

		// 랜덤 정렬을 위한 Expressions 생성
		OrderSpecifier<?> randomOrder = Expressions.numberTemplate(Double.class, "function('RAND')").asc();

		// 서브쿼리를 사용하여 sigungu_name 값을 가져옴
		JPQLQuery<String> sigunguNameSubQuery = JPAExpressions
			.select(sigungu.name)
			.from(sigungu)
			.where(sigungu.code.eq(member.sigunguCode));

		return queryFactory
			.select(Projections.fields(MemberMatchDto.class,
				member.nickName,
				new CaseBuilder()
					.when(member.memberSettings.isProfileVisible.isFalse())
					.then("https://climbing-day-bucket.s3.ap-northeast-2.amazonaws.com/climbing-day-no-image.jpg")
					.otherwise(member.profileImage).as("profileImage"),
				Expressions.stringTemplate("({0})", sigunguNameSubQuery).as("sigunguName") // 서브쿼리를 통해 가져온 sigunguName
			))
			.from(member)
			.where(
				member.memberSettings.isMatchingEnabled.isTrue() // 매칭이 활성화된 경우
					.and(
						member.id.ne(memberId)
					)
					.and(
						haversine.loe(distance) // haversine 거리가 지정된 거리보다 작거나 같음
					)
			)
			.orderBy(randomOrder) // 랜덤으로 정렬
			.limit(limit) // 제한 수
			.fetch();
	}

	private BooleanExpression memberId(Long id) {
		return member.id.eq(id);
	}
}
