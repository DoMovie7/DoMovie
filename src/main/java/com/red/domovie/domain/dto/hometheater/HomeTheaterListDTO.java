package com.red.domovie.domain.dto.hometheater;

import lombok.*;

import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeTheaterListDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;
    private String thumbnailImageUrl;  // 대표 이미지 경로 추가
    private String category;
}
