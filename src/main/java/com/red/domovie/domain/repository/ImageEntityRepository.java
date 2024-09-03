package com.red.domovie.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.domovie.domain.entity.UserEntity;

public interface ImageEntityRepository extends JpaRepository<UserEntity, Long> {

}
