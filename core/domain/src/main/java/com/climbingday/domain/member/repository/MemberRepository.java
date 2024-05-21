package com.climbingday.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustom {
	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
}
