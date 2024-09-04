package com.red.domovie.domain.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import com.red.domovie.domain.dto.movieDetail.PostMovieRatingDTO;
import com.red.domovie.domain.dto.movieDetail.getMovieRatingDTO;



@Mapper
public interface MovieDetailMapper {

	void saveMovieRating(@Param("userId") Long userId,@Param("dto") PostMovieRatingDTO dto);

	getMovieRatingDTO findUserMovieRating(@Param("userId")Long userId,@Param("movieID")String movieID);

	List<getMovieRatingDTO> findMovieRatingList(@Param("movieID") String movieID, PageRequest pageRequest);

}
