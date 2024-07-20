package com.climbingday.security.service;

import static com.climbingday.enums.MemberErrorCode.*;
import static com.climbingday.enums.member.EStatus.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.climbingday.domain.member.Member;
import com.climbingday.domain.member.repository.MemberRepository;
import com.climbingday.security.exception.CustomSecurityException;

import lombok.RequiredArgsConstructor;

@Service
@Qualifier("memberUserDetailsService")
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomSecurityException(CHECK_ID_OR_PASSWORD));

        // 소셜 로그인의 경우 소셜 로그인으로 유도
        if(member.getProvider() != null) {
            throw new CustomSecurityException(SOCIAL_LOGIN_MEMBER);
        }

        if (member.getStatus().equals(INACTIVE)) {
            throw new CustomSecurityException(DELETE_MEMBER);
        }
        return UserDetailsImpl.from(member);
    }
}
