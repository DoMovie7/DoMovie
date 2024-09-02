package com.red.domovie.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.red.domovie.domain.entity.RecommendEntity;


public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> {

}
