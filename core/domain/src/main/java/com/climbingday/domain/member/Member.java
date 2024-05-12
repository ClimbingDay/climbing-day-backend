package com.climbingday.domain.member;

import java.sql.Date;

import com.climbingday.domain.member.enums.ERoles;
import com.climbingday.domain.member.enums.EStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = "email"),
	@UniqueConstraint(columnNames = "phoneNumber")
})
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String email;						// 아이디(이메일)

	@Setter
	private String password;					// 비밀번호

	private String name;						// 이름

	private Date birthDate;						// 생년월일

	private String phoneNumber;					// 전화번호

	@Enumerated(EnumType.STRING)
	private EStatus status = EStatus.PENDING;	// 상태

	@Enumerated(EnumType.STRING)
	private ERoles roles = ERoles.ROLE_USER;
}
