package com.red.domovie.domain.entity;


import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
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

@DynamicUpdate
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "search_trend")
public class SearchTrendEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 각 검색 트렌드 기록의 고유 식별자
    
    @Column(unique = true)
    private String keyword; // 트렌드로 기록된 검색 키워드
    
    private int searchCount; // 해당 키워드가 검색된 총 횟수


    public void incrementSearchCount() {
        this.searchCount++;
    }
    
    // keyword를 인자로 받는 생성자
    public SearchTrendEntity(String keyword) {
        this.keyword = keyword;
        this.searchCount = 1; // 기본 searchCount 값을 설정할 수 있습니다.
    }
    
   
}
