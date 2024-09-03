package com.red.domovie.service.impl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import com.fasterxml.jackson.databind.JsonNode;
import com.red.domovie.common.util.KmdbMovieUtil;
import com.red.domovie.domain.dto.movieDetail.GetMovieDetailDTO;
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
        if(response.getStatusCode() == HttpStatus.OK) {
        	GetMovieDetailDTO movieDetail=GetMovieDetailDTO.toDTO(responseBody);
			 model.addAttribute("movieDetail", movieDetail);
			 
			 System.out.println("Movie Detail DTO:");
		     System.out.println(movieDetail.toString());
			
	        	
	        }
	
	}

	
	
	
}
