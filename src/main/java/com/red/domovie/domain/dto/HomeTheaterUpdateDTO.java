package com.red.domovie.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeTheaterUpdateDTO {
    private Long id;
    private String title;
    private String content;
    private String thumbnailImageUrl;  // 썸네일 이미지 경로 추가
}
