package com.red.domovie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.red.domovie.domain.dto.movie.SearchTrendDTO;
import com.red.domovie.domain.entity.SearchTrendEntity;
import com.red.domovie.domain.repository.SearchTrendRepository;
import com.red.domovie.service.SearchTrendService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchTrendServiceProcess implements SearchTrendService{
	
	private final SearchTrendRepository searchTrendRepository;
	

	  @Override
	    @Transactional
	    public void saveOrUpdateKeyword(String keyword) {
	        try {
	            System.out.println("Attempting to save or update keyword: " + keyword);
	            SearchTrendEntity trend = searchTrendRepository.findByKeyword(keyword)
	                .orElse(new SearchTrendEntity(keyword));

	            trend.incrementSearchCount();
	            SearchTrendEntity savedTrend = searchTrendRepository.save(trend);
	            System.out.println("Saved trend: " + savedTrend);
	        } catch (Exception e) {
	            System.err.println("Error saving keyword: " + e.getMessage());
	            e.printStackTrace();
	            throw e; // 예외를 다시 던져서 트랜잭션이 롤백되도록 합니다.
	        }
	    }

    @Override
    public List<SearchTrendDTO> getTopSearchTrends(int limit) {
        return searchTrendRepository.findTopSearchTrends(limit).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private SearchTrendDTO convertToDTO(SearchTrendEntity entity) {
        return new SearchTrendDTO(entity.getId(), entity.getKeyword(), entity.getSearchCount());
    }
	
	

}
