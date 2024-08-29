package com.red.domovie.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class HomeTheaterDetailDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;
    private String content;
    private List<CommentDTO> comments;
    private String thumbnailImageUrl;  // 대표 이미지 경로 추가

}
