package com.red.domovie.domain.dto.recommend;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RecommendListDTO {
	
	
	private long no; //항목 번호
	private String title; //항목 제목
	private String user; //항목 글쓴이
	private LocalDateTime createdAt; //항목 날짜와 시간
	private String imgUrl; // 항목 이미지 url
	private int commentCount; //항목 댓글 수 

}
