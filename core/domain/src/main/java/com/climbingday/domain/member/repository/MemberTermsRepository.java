package com.climbingday.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.member.MemberTerms;

public interface MemberTermsRepository extends JpaRepository<MemberTerms, Long> {
}
