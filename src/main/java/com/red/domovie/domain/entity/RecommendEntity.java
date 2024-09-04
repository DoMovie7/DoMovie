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
    private Genre genre; // 장르
    
    @ManyToOne
    @JoinColumn(name = "author")
    private UserEntity author; // 작성자
    
    
    //UserEntity 를 세팅하고 RecommendEntity 리턴하는
    public RecommendEntity author(UserEntity author) {
    	this.author=author;
    	return RecommendEntity.this;
    }
    
    private int commentCount; // 조회수
    
    
    
    private String imgUrl; // 항목 이미지 url
}

