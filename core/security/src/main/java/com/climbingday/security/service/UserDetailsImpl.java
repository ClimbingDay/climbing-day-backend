package com.climbingday.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.climbingday.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
	private long id;
	private String email;
	@JsonIgnore
	private String password;
	private String nickName;
	private List<GrantedAuthority> authorities;

	public UserDetailsImpl(long id, String email, String password, String nickName, List<GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickName = nickName;
		this.authorities = authorities;
	}

	public static UserDetails from(Member member) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(member.getRoles().name()));

		return new UserDetailsImpl(
			member.getId(),
			member.getEmail(),
			member.getPassword(),
			member.getNickName(),
			authorities
		);
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
