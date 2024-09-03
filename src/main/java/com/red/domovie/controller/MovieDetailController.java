package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.red.domovie.service.MovieDetailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MovieDetailController {
	
	private final MovieDetailService movieDetailService;
	
	@GetMapping("/contents/{movieID}")
	public String MovieDetail(@PathVariable(name = "movieID") String movieID,Model model) {
		
		System.out.println("movieID:"+movieID);
		
		movieDetailService.findMovieDetail(movieID,model);
		
		
		return "views/movieDetail/list";
	}
	

}
