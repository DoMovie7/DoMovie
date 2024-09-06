package com.red.domovie.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.domain.dto.movie.KmdbMovieDTO;
import com.red.domovie.service.MovieApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MovieSearchController {
	private final MovieApiService movieApiService;
	
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


}
