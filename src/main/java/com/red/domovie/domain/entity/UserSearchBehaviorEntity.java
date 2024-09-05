package com.red.domovie.domain.entity;

import org.hibernate.annotations.DynamicUpdate;

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
@Table(name = "user_search_behavior")
public class UserSearchBehaviorEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 각 검색 행동 기록의 고유 식별자
    
    @ManyToOne
    @JoinColumn(name = "userId") // 검색을 수행한 사용자 (다대일 관계)
    private UserEntity userId;
    
    private String searchQuery; // 사용자가 입력한 검색어
    private String clickedResult; // 사용자가 클릭한 검색 결과의 식별자 또는 URL
    private Long sessionDuration; // 검색 세션의 지속 시간 (초 단위)
}
