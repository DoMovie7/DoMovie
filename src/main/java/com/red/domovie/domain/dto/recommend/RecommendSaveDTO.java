package com.red.domovie.domain.dto.recommend;

import com.red.domovie.domain.entity.Genre;

import lombok.Data;

@Data
public class RecommendSaveDTO {
	
	private String title; //제목  
    private String content; //내용
    //private MultipartFile image; //이미지업로드
    private Genre genre;//문자열이지만 Enum으로 매핑가능
    
    private String bucketKey; //처음에는 tempKey
	private String orgName;
	private String newName;
	private String imgUrl; // 포스터 이미지 URL
	
	public RecommendSaveDTO update(String imgUrl, String destinationKey) {
		this.imgUrl=imgUrl;
		this.bucketKey=destinationKey;//
		return this;
	}
}
