package com.red.domovie.domain.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
	
	@JsonProperty("DOCID")
	private String docid;
    private String posters;
    private String title;
    private String prodYear;
    private String nation;
    private Plots plots;
    
    public String poster() {
    	String[] strs=posters.split("[|]");
    	return strs[0];
    }
}
