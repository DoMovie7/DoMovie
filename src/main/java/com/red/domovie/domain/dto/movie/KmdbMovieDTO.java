package com.red.domovie.domain.dto.movie;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KmdbMovieDTO {
	
	
	@JsonProperty("DOCID")
	private String docid;
    private String posters;
    private String rating;
    private String title;
    private String prodYear;
    private String nation;
    private String genre;
    private String repRlsDate;
    
    //문자열로 받은 날짜 yyyymmdd 를 포맷형식으로 yyyy-mm-dd로 출력 
    public String dotDate() {
    	if (repRlsDate == null || repRlsDate.isEmpty()) {
            return null; // Or any default value you prefer
        }
    	int date=Integer.parseInt(repRlsDate);//yyyymmdd
    	int day=date%100;
    	int month=date/100%100; //yyyymm
    	int year=date/100/100; //yyyymm
    	
    	//return LocalDate.of(year, month, day);
    	
    	String formatedDate=String.format("%04d-%02d-%02d", year,month,day);
    	return formatedDate;
    }
    
    private Plots plots;
    
    public String poster() {
        if (posters != null && !posters.isEmpty()) {
            String[] strs = posters.split("[|]");
            return strs.length > 0 ? strs[0] : null;
        }
        return null; // Return null if posters is null or empty
    }
    
    public String getRepRlsDate() {
        if (repRlsDate == null || repRlsDate.isEmpty()) {
            return "Unknown"; // Or any default value you prefer
        }
        return repRlsDate;
    }
    
}
