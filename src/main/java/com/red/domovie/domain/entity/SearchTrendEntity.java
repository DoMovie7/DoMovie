package com.red.domovie.domain.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import com.red.domovie.domain.enums.Tier;

import jakarta.persistence.Entity;
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
import lombok.ToString;

@DynamicUpdate
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@ToString
@Table(name = "search_trend")
public class SearchTrendEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 각 검색 트렌드 기록의 고유 식별자
    private String keyword; // 트렌드로 기록된 검색 키워드
    private int searchCount; // 해당 키워드가 검색된 총 횟수
   
    
   
}
