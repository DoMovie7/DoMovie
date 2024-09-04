package com.red.domovie.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import com.fasterxml.jackson.databind.JsonNode;
import com.red.domovie.common.util.KmdbMovieUtil;
import com.red.domovie.domain.dto.movieDetail.GetMovieDetailDTO;
import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;
import com.red.domovie.domain.dto.movieDetail.getMovieRatingDTO;
import com.red.domovie.domain.entity.MovieRatingsEntity;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.mapper.MovieDetailMapper;
import com.red.domovie.service.MovieDetailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieDetailServiceProcess implements MovieDetailService {
	
	//영화 상세 정보 얻어오는 유틸
	private final KmdbMovieUtil kmdbMovieUtil;
	private final MovieDetailMapper movieDetailMapper;

	
	//영화 id로 상세정보,리뷰 얻어오기
	@Override
	public void findMovieDetail(String movieID, Model model,int page) {
		
		//영화 상세정보 가져오기
		ResponseEntity<JsonNode> response = kmdbMovieUtil.getDtailMovie(movieID);
		
		JsonNode responseBody = response.getBody();
		
        System.out.println("API Response: " + responseBody);
        if(response.getStatusCode() == HttpStatus.OK) {
        	GetMovieDetailDTO movieDetail=GetMovieDetailDTO.toDTO(responseBody);
			 model.addAttribute("movieDetail", movieDetail);
			 
			 System.out.println("Movie Detail DTO:");
		     System.out.println(movieDetail.toString());
			
	        	
	        }
        
        //영화에 따른 전체 리뷰 가져오기
        
        
        int offset = page *6;
       
        List<getMovieRatingDTO> movieRatingList = movieDetailMapper.findMovieRatingList(movieID,PageRequest.of(page,6));
        
        model.addAttribute("ratingPage", movieRatingList);
	
	
    
	
	

	}





	//별점 쓰기 
	@Override
	public void saveMovieRating(Long userId, PostMovieRatingDTO dto) {
		
		System.out.println(userId);

		
		//별점코멘트 저장
		movieDetailMapper.saveMovieRating(userId,dto);
		
		
		
		
		
	}


    //특정 유저의 정보 가져오기 
	@Override
	public void findUserMovieRating(Long userId, String movieID, Model model) {
		
		
		getMovieRatingDTO userMovieRating = movieDetailMapper.findUserMovieRating(userId,movieID);
		System.out.println(userMovieRating);
		
		model.addAttribute("userMovieRating", userMovieRating);
		
		
		
	}

	
	
	
}
