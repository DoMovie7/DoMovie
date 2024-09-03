package com.red.domovie.domain.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxOfficeDTO {
	
    @JsonProperty("DOCID")
    private String DOCID;
	private long movieCd;
	private String posters;
    private String movieNm;
    private String openDt;
    private long rank;
    private long audiCnt;
    private long audiAcc;
    private String movieId;

    
    public String poster() {
    	String[] strs=posters.split("[|]");
    	return strs[0];
    }





}
