package com.red.domovie.domain.dto.recommend;

import com.red.domovie.domain.entity.Genre;

import lombok.Data;

@Data
public class RecommendUpdateDTO {
	
	private String title; //제목  
    private String content; //내용
    //private MultipartFile image; //이미지업로드
    private Genre genre;//문자열이지만 Enum으로 매핑가능
        
}
