package com.red.domovie.domain.dto.recommend;

import java.time.LocalDateTime;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.entity.RecommendEntity;
import com.red.domovie.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// RecommendListDTO 클래스는 추천 목록을 표현하는 데이터 전송 객체(DTO)입니다.
// @Builder와 @Getter 어노테이션을 사용하여 빌더 패턴과 Getter 메서드를 자동으로 생성합니다.
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class RecommendListDTO {

    private long id; // 항목 번호
    private String title; // 항목 제목
    private String email; // 항목 글쓴이의 이메일
    private String userName; // 항목 글쓴이의 사용자 이름
    private LocalDateTime createdAt; // 항목 작성 날짜와 시간
    private String imgUrl; // 항목 이미지 URL
    private int commentCount; // 항목 댓글 수
    
    private ProfileDTO author;
    
    public RecommendListDTO author(ProfileDTO author) {
    	this.author=author;
    	return this;
    }
	
}
