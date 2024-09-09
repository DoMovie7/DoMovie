package com.red.domovie.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.red.domovie.domain.entity.RecommendEntity;
import com.red.domovie.domain.entity.UserEntity;

// RecommendRepository 인터페이스는 RecommendEntity를 데이터베이스와 연동하기 위한 저장소 역할을 합니다.
// JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
// 제네릭 타입으로 RecommendEntity와 그 기본 키의 타입(Long)을 지정합니다.

public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> {

	int countByAuthor(UserEntity user);

	List<RecommendEntity> findByAuthor(UserEntity author);

}
