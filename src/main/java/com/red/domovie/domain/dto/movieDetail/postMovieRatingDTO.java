package com.red.domovie.domain.dto.movieDetail;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class postMovieRatingDTO {
	
	private String movieId;
	private float rating; 
	private String comments;

}
