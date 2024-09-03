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
