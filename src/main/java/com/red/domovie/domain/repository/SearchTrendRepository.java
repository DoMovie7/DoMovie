package com.red.domovie.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.domovie.domain.entity.SearchTrendEntity;

public interface SearchTrendRepository extends JpaRepository<SearchTrendEntity, Long> {
}