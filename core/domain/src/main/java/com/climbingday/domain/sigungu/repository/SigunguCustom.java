package com.climbingday.domain.sigungu.repository;

import java.util.List;

import com.climbingday.dto.sigungu.SigunguDto;

public interface SigunguCustom {
	List<SigunguDto> getSigunguList(String name);
}
