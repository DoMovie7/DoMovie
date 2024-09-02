package com.red.domovie.domain.entity;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@DynamicUpdate
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "movie_ratings", 
uniqueConstraints = {@UniqueConstraint(columnNames = {"movieId", "user_id"})})
@Entity
public class MovieRatingsEntity {
	
	//유저와 무비 아이디를 복합키로 쓰는 고유 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
	
	
	//영화 번호
	@Column(nullable = false,columnDefinition = "varchar(100)")
	private String movieId;
	
    // 별점
    @Column(nullable = false)
    private float rating; 
	
    // 코멘트
    @Lob 
    @Column(nullable = false)  
    private String comments;
    
    // 별점을 단 사용자와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
	
	
	

}
