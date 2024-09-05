package com.red.domovie.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.red.domovie.domain.entity.SearchTrendEntity;
import com.red.domovie.domain.repository.SearchTrendRepository;
import com.red.domovie.service.SearchTrendService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchTrendServiceProcess implements SearchTrendService{
	
	private final SearchTrendRepository trendRepository;

    @Override
    public List<SearchTrendEntity> getAllSearchTrends() {
        return trendRepository.findAll();
    }
}
