package com.red.domovie.domain.dto.movieDetail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class getMovieRatingDTO {
	
	private Long userId;
	private String nickName;
	private String movieId;
	private float rating; 
	private String comments;

}
