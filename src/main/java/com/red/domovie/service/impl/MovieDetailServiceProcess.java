package com.red.domovie.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.red.domovie.common.util.KmdbMovieUtil;
import com.red.domovie.service.MovieDetailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieDetailServiceProcess implements MovieDetailService {
	
	private final KmdbMovieUtil kmdbMovieUtil;

	@Override
	public void findMovieDetail(String movieID, Model model) {
		
		ResponseEntity<JsonNode> response = kmdbMovieUtil.getDtailMovie(movieID);
		JsonNode responseBody = response.getBody();
        System.out.println("API Response: " + responseBody);
		
		
	}

}
