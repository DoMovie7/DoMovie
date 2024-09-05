package com.red.domovie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rabbitmq.client.AMQP.Basic.Return;
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
	
	//영화 상세 정보 
	@GetMapping("/movies/detail/{movieID}")
	public String MovieDetail(@PathVariable(name = "movieID") String movieID,Model model,@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(name = "page" ,defaultValue = "0") int page) {
		
		System.out.println("movieID:"+movieID);
		//
		movieDetailService.findMovieDetail(movieID,model);
		
		
		return "views/movieDetail/list";
	}
	
	
	//비동기 처리를 위한 사용자 리뷰 요청
	@GetMapping("/movies/detail/{movieID}/usercomments")
	public String findUserComments(@PathVariable(name = "movieID") String movieID,@AuthenticationPrincipal CustomUserDetails userDetails,Model model){
		
		System.out.println(movieID);
		
		if(userDetails != null) {
			System.out.println("특정리뷰 가져오기 작동중");
			 movieDetailService.findUserMovieRating(userDetails.getUserId(),movieID,model);
			
		}
		
		return "views/movieDetail/listFragments :: userReviewSection";
			
		}

	
	
	//비동기 처리를 위한 전체 리뷰 요청
	@GetMapping("/movies/detail/{movieID}/comments")
	public String findAllComments(@PathVariable(name = "movieID") String movieID,Model model){
		
		
		
	    //영화에 맞는 모든 리뷰 가져오기
		movieDetailService.findAllComments(movieID,model);
		
		return "views/movieDetail/listFragments :: reviewList";
			
		}
	
	
	
	
	//리뷰 저장
	@ResponseBody
	@PostMapping("/movies/detail/comment/write")
	public String  postMovieRating(@RequestBody PostMovieRatingDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails ,Model model) {
	
    	
		System.out.println(dto);
		if(userDetails != null) {
			//리뷰 저장
			movieDetailService.saveMovieRating(userDetails.getUserId(),dto);
			
			
		}
		
		  return null;
		  
	
	}
	
	

}