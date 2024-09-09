package com.red.domovie.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.red.domovie.domain.entity.SearchTrendEntity;

public interface SearchTrendRepository extends JpaRepository<SearchTrendEntity, Long>{
	
    Optional<SearchTrendEntity> findByKeyword(String keyword);

    @Query("SELECT s FROM SearchTrendEntity s ORDER BY s.searchCount DESC")
    List<SearchTrendEntity> findTopSearchTrends(int limit);

	

}
