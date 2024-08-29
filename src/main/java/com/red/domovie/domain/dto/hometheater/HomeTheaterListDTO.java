package com.red.domovie.domain.dto.hometheater;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HomeTheaterListDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;
    private String thumbnailImageUrl;  // 대표 이미지 경로 추가
}
