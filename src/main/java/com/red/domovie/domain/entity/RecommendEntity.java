package com.red.domovie.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recommend")
public class RecommendEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키
    private String title; // 제목
    private String content; // 내용
    private String genre; // 장르
    private String author; // 작성자
    private int commentCount; // 조회수
    private LocalDateTime createdDate; // 날짜
    private String imgUrl; // 항목 이미지 url
}

