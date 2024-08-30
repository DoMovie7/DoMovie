package com.red.domovie.domain.dto.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class KmdbMovieResponse {
	
	@JsonProperty("Result")
	private List<MovieDTO> result;

}
