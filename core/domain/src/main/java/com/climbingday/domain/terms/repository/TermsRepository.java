package com.climbingday.domain.terms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.terms.Terms;

public interface TermsRepository extends JpaRepository<Terms, Long>, TermsCustom {
	Optional<Terms> findByType(String type);
}
