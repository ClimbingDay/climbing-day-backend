package com.climbingday.domain.terms.repository;

import java.util.List;

import com.climbingday.dto.terms.TermsDto;

public interface TermsCustom {
	List<TermsDto> getAllTerms();

	List<TermsDto> getRequiredTerms();
}
