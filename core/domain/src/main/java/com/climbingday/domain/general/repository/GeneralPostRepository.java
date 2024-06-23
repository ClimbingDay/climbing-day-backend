package com.climbingday.domain.general.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.general.GeneralPost;

public interface GeneralPostRepository extends JpaRepository<GeneralPost, Long>, GeneralPostCustom {

}
