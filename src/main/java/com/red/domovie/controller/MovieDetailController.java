package com.red.domovie.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;
import com.red.domovie.security.CustomUserDetails;
import com.red.domovie.service.MovieDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
public class MovieDetailController {
	
	private final MovieDetailService movieDetailService;
	
	@GetMapping("/movies/detail/{movieID}")
	public String MovieDetail(@PathVariable(name = "movieID") String movieID,Model model,@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(name = "page" ,defaultValue = "0") int page) {
		
		System.out.println("movieID:"+movieID);
		//
		movieDetailService.findMovieDetail(movieID,model,page);
		
		if(userDetails != null) {
				
			System.out.println("특정리뷰 가져오기 작동중");
				movieDetailService.findUserMovieRating(userDetails.getUserId(),movieID,model);
				
			}
		
		
		return "views/movieDetail/list";
	}
	
	
	
	@PostMapping("/movies/detail/comment/write")
	public String  postMovieRating(@RequestBody PostMovieRatingDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails ,Model model) {
		
    	Map<String, String> response = new HashMap<>();
    	
		System.out.println(dto);
		if(userDetails != null) {
			//리뷰 저장
			movieDetailService.saveMovieRating(userDetails.getUserId(),dto);
			
			movieDetailService.findUserMovieRating(userDetails.getUserId(), dto.getMovieId(), model);
            // 전체 리뷰 목록 업데이트
           // movieDetailService.findMovieRatingList(dto.getMovieId(), 0, 6, model);
			
		}
		
		  return "views/movieDetail/listFragments :: userReviewSection, reviewList";
		  
	
	}
	
	

}