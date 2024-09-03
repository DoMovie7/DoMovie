package com.red.domovie.domain.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class Plot {
	private String plotText;
	private String plotLang;

}
