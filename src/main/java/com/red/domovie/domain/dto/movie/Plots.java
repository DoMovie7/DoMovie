package com.red.domovie.domain.dto.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class Plots {
	private List<Plot> plot;

}
