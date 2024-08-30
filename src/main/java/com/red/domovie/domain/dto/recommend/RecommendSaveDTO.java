package com.red.domovie.domain.dto.recommend;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RecommendSaveDTO {
	
	private String title; //제목  
    private String content; //내용
    private MultipartFile image; //이미지업로드
}
