package com.red.domovie.domain.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import com.red.domovie.domain.dto.movieDetail.GetMovieRatingDTO;
import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;




@Mapper
public interface MovieDetailMapper {

	void saveMovieRating(@Param("userId") Long userId,@Param("dto") PostMovieRatingDTO dto);

	GetMovieRatingDTO findUserMovieRating(@Param("userId")Long userId,@Param("movieID")String movieID);


	List<GetMovieRatingDTO> findMovieRatingList(@Param("movieID") String movieID);

	long countMovieRatings(@Param("movieID") String movieID);
	
	

}