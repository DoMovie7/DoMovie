package com.red.domovie.service;

import org.springframework.ui.Model;

import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;

public interface MovieDetailService {

	void findMovieDetail(String movieID, Model model, int page);

	void saveMovieRating(Long userId, PostMovieRatingDTO dto);

	void findUserMovieRating(Long userId, String movieID, Model model);

	String getUserReviewSectionHtml(Long userId, String movieId);

	String getReviewListHtml(String movieId, int i, int j);

}
