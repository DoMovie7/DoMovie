package com.red.domovie.domain.dto.movie;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KmdbMovieDTO {
	
	private static final String DEFAULT_POSTER = "/img/index/no-movie-img.jpg"; // 디폴트 이미지 경로
	private static final String DEFAULT_RATING = "심의중"; // 디폴트 등급 정보
	
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
    
    @JsonProperty("dotDate") //스크립트에서 사용가능하게 인코딩
    public String getDotDate() {
        return dotDate();
    }
    
    private Plots plots;
    
    @JsonProperty("posterUrl") //스크립트에서 사용가능하게 인코딩
    public String getPosterUrl() {
        return poster();
    }
    
    
    
    public String poster() {
        if (posters != null && !posters.isEmpty()) {
            String[] strs = posters.split("[|]");
            return strs.length > 0 && !strs[0].isEmpty() ? strs[0] : DEFAULT_POSTER;
        }
        return DEFAULT_POSTER; // posters가 null이거나 비어 있으면 디폴트 이미지 반환
    }
    
    public String getRepRlsDate() {
        if (repRlsDate == null || repRlsDate.isEmpty()) {
            return "Unknown"; // Or any default value you prefer
        }
        return repRlsDate;
    }
    
    // rating 필드에 값이 없을 경우 "심의중" 반환
    public String getRating() {
        return (rating == null || rating.isEmpty()) ? DEFAULT_RATING : rating;
    }
    
}
