package com.red.domovie.domain.dto.movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTrendDTO {
	
	private Long id;
	private String keyword;
	private int searchCount;
	
    public SearchTrendDTO(Long id, String keyword, int searchCount) {
        this.id = id;
        this.keyword = keyword;
        this.searchCount = searchCount;
    }

}
