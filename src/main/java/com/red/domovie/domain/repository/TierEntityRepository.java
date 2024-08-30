package com.red.domovie.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.domovie.domain.entity.TierEntity;
import com.red.domovie.domain.entity.UserEntity;

public interface TierEntityRepository extends JpaRepository<TierEntity, Long>{



}
