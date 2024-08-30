package com.red.domovie.domain.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoxOfficeDTO {
	
    private long box_office_id;
    private String movieNm;
    private String openDt;
    private long rank;
    private long audiCnt;
    private long audiAcc;
    

}
