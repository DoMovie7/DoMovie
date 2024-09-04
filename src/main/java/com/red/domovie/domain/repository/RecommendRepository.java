package com.red.domovie.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.red.domovie.domain.entity.RecommendEntity;
import com.red.domovie.domain.entity.UserEntity;


public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> {

	int countByAuthor(UserEntity user);

	List<RecommendEntity> findByAuthor(UserEntity author);

}
