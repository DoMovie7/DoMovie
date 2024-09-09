package com.red.domovie.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.red.domovie.common.util.KmdbMovieUtil;
import com.red.domovie.domain.dto.movieDetail.GetAverageRatingDTO;
import com.red.domovie.domain.dto.movieDetail.GetMovieDetailDTO;
import com.red.domovie.domain.dto.movieDetail.GetMovieRatingDTO;
import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;
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

	
	//영화 id로 상세정보 얻어오
	@Override
	public void findMovieDetail(String movieID, Model model) {
		
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
        

	}
	
	
	
    //사용자 리뷰 가져오기
	@Override
	public void findUserMovieRating(Long userId, String movieID,Model model) {
		
	  
	    model.addAttribute("userMovieRating", movieDetailMapper.findUserMovieRating(userId,movieID));
		
	}
	

	// 영화에 따른 전체 리뷰를 가져오는 메서드
	@Override
	public void findAllComments(String movieID, Model model) {
		
		List<GetMovieRatingDTO> movieRatingList= movieDetailMapper.findMovieRatingList(movieID);
		model.addAttribute("movieRatingList", movieRatingList);

	}






	//별점 쓰기 
	@Override
	public void saveMovieRating(Long userId, PostMovieRatingDTO dto) {
		
		System.out.println(userId);

		
		//별점코멘트 저장
		movieDetailMapper.saveMovieRating(userId,dto);
		
		
	}


    //리뷰수정
	@Override
	public void updateMovieRating(Long userId, PostMovieRatingDTO dto) {
		System.out.println(userId);

		
		//별점수정코멘트 업데이트
		movieDetailMapper.updateMovieRating(userId,dto);
		
	}


    //평균 별점
	@Override
	public void findAverageRating(String movieID, Model model) {
		
		GetAverageRatingDTO dto=movieDetailMapper.findAverageRating(movieID);
		System.out.println(dto);
		
		if(dto ==null){
			dto = new GetAverageRatingDTO(); 
			dto.setAvg(0);
		}
		model.addAttribute("averageRating",dto);
		
	}


    //리뷰 삭제
	@Override
	public void deleteMovieRating(Long userId, String movieId) {
		System.out.println(userId+"유저>>>>>>>>"+movieId);
		String cleanMovieId = movieId.replace("\"", "");
		movieDetailMapper.deleteMovieRating(cleanMovieId,userId);
		
	}





	

	
}