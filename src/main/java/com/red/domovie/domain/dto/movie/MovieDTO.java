package com.red.domovie.domain.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
	
    @JsonProperty("DOCID")
    private String DOCID;
	private String movieCd;
    private String poster;
    private String movieNm;
    private String movieNmEn;
    private String prdtYear;
    private String openDt;
    private String typeNm;
    private String nation;
    private String repGenreNm;
    private String genreAlt;
    private String movieId;
    private String prdtStatNm;


	
    /*
    public String poster() {
    	String[] strs=posters.split("[|]");
    	return strs[0];
    }
    /*/
	
	
	/*
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
    */
}
