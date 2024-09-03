package com.red.domovie.domain.dto.movieDetail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Setter
@Getter
public class postMovieRatingDTO {
	
	private String movieId;
	private float rating; 
	private String comments;

}
