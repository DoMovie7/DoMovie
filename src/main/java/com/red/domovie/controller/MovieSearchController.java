package com.red.domovie.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.domain.dto.movie.KeywordRequest;
import com.red.domovie.domain.dto.movie.KmdbMovieDTO;
import com.red.domovie.domain.dto.movie.SearchTrendDTO;
import com.red.domovie.service.MovieApiService;
import com.red.domovie.service.SearchTrendService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MovieSearchController {
	private final MovieApiService movieApiService;
	private final SearchTrendService searchTrendService;
	
    @GetMapping("/autocomplete")
    public ResponseEntity<List<KmdbMovieDTO>> getAutoCompleteSuggestions(@RequestParam(name = "query") String query) {
        try {
            List<KmdbMovieDTO> suggestions = movieApiService.getAutoCompleteSuggestions(query);
            System.out.println("Suggestions: " + suggestions);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    
    @PostMapping("/search/save")
    public ResponseEntity<Void> saveSearchKeyword(@RequestBody KeywordRequest keywordRequest) {
        System.out.println("Received search request for keyword: " + keywordRequest.getKeyword());
        searchTrendService.saveOrUpdateKeyword(keywordRequest.getKeyword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/trends")
    public ResponseEntity<List<SearchTrendDTO>> getTopSearchTrends(
        @RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<SearchTrendDTO> trends = searchTrendService.getTopSearchTrends(limit);
        return ResponseEntity.ok(trends);
    }

    

}
