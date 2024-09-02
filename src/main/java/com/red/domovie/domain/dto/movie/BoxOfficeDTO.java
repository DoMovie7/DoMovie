package com.red.domovie.domain.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxOfficeDTO {
	
	
	private long movieCd;
	private String posters;
    private String movieNm;
    private String openDt;
    private long rank;
    private long audiCnt;
    private long audiAcc;
    
    public String poster() {
    	String[] strs=posters.split("[|]");
    	return strs[0];
    }
    

}
