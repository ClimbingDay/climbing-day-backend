package com.climbingday.domain.general.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.climbingday.domain.general.GeneralComment;

public interface GeneralCommentRepository extends JpaRepository<GeneralComment, Long>, GeneralCommentCustom {

}
