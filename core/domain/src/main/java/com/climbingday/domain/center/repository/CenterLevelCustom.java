package com.climbingday.domain.center.repository;

import java.util.List;

import com.querydsl.core.Tuple;

public interface CenterLevelCustom {
	List<Tuple> getCenterLevels(Long centerId);
}
