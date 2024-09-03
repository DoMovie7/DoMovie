package com.red.domovie.domain.dto.recommend;

import java.time.LocalDateTime;

import com.red.domovie.domain.entity.RecommendEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RecommendListDTO {
	
	
	private long no; //항목 번호
	private String title; //항목 제목
	private String email; //항목 글쓴이
	private String userName; //항목 글쓴이
	private LocalDateTime createdAt; //항목 날짜와 시간
	private String imgUrl; // 항목 이미지 url
	private int commentCount; //항목 댓글 수 

	// Service 계층에서 엔티티 -> DTO 변환 예시
	public RecommendListDTO convertToDTO(RecommendEntity entity) {
	    return RecommendListDTO.builder()
	        .no(entity.getId())
	        .title(entity.getTitle())
	        .email(entity.getAuthor().getEmail())  // 엔티티의 author 필드를 user로 매핑
	        .userName(entity.getAuthor().getUserName())  // 엔티티의 author 필드를 user로 매핑
	        .imgUrl(entity.getImgUrl())
	        .createdAt(entity.getCreatedDate())  // 엔티티의 createdDate 필드를 createdAt으로 매핑
	        .commentCount(entity.getCommentCount())
	        .build();
	}
}