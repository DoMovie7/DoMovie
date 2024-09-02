package com.red.domovie.domain.dto.hometheater;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeTheaterUpdateDTO {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String thumbnailImageUrl;  // 썸네일 이미지 경로 추가
}
