package com.red.domovie.service;

import java.util.List;

import com.red.domovie.domain.dto.movie.SearchTrendDTO;

public interface SearchTrendService {

    void saveOrUpdateKeyword(String keyword);
    List<SearchTrendDTO> getTopSearchTrends(int limit);
}
