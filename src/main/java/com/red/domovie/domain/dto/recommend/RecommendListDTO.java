package com.red.domovie.domain.dto.recommend;

import java.time.LocalDateTime;

import com.red.domovie.domain.entity.RecommendEntity;

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
    
    

    // Service 계층에서 엔티티를 DTO로 변환하는 예시 메서드입니다.
    public RecommendListDTO convertToDTO(RecommendEntity entity) {
        // RecommendEntity 객체를 RecommendListDTO로 변환합니다.
        return RecommendListDTO.builder()
            .id(entity.getId()) // 엔티티의 ID를 DTO의 항목 번호로 설정합니다.
            .title(entity.getTitle()) // 엔티티의 제목을 DTO의 항목 제목으로 설정합니다.
            .email(entity.getAuthor().getEmail()) // 엔티티의 author 필드에서 작성자의 이메일을 DTO에 설정합니다.
            .userName(entity.getAuthor().getUserName()) // 엔티티의 author 필드에서 작성자의 사용자 이름을 DTO에 설정합니다.
            .imgUrl(entity.getImgUrl()) // 엔티티의 이미지 URL을 DTO에 설정합니다.
            .createdAt(entity.getCreatedAt()) // 엔티티의 작성 날짜와 시간을 DTO에 설정합니다.
            .commentCount(entity.getCommentCount()) // 엔티티의 댓글 수를 DTO에 설정합니다.
            .build(); // 모든 필드를 설정한 후 DTO 객체를 빌드하여 반환합니다.
    }


	
}
