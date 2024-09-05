package com.red.domovie.service;

import java.util.List;

import org.springframework.ui.Model;

import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;
import com.red.domovie.domain.dto.movieDetail.getMovieRatingDTO;

public interface MovieDetailService {
	//영화 상세정보 가져오기
	void findMovieDetail(String movieID, Model model);
	//별점 쓰기 
	void saveMovieRating(Long userId, PostMovieRatingDTO dto);
	//사용자 리뷰 가져오기
	void findUserMovieRating(Long userId, String movieID, Model model);
    //전체 사용자리뷰 가져오기
	void findAllComments(String movieID, Model model);

}
