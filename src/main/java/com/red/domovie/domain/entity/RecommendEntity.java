package com.red.domovie.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@DynamicUpdate
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "recommend")
public class RecommendEntity extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키
    
    private String title; // 제목
    
    private String content; // 내용
    
    @Enumerated(EnumType.STRING)
    private Genre genre; // 장르 (열거형 타입으로 저장)
    
    @ManyToOne
    @JoinColumn(name = "author")
    private UserEntity author; // 작성자 (다대일 관계 설정)
    
    // UserEntity를 세팅하고 RecommendEntity를 반환하는 메소드
    public RecommendEntity author(UserEntity author) {
    	this.author = author;
    	return RecommendEntity.this;
    }
    
    private int commentCount; // 조회수
    
    private String imgUrl; // 포스터 이미지 URL
    private String bucketKey; // 버킷key
    private String orgName;//파일 원본이름
}
