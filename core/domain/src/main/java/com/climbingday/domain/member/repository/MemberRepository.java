package com.climbingday.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustom {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByEmailAndProvider(String email, String provider);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByNickName(String nickName);
}
