package com.red.domovie.domain.dto.hometheater;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeTheaterDetailDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;
    private String content;
    private List<CommentListDTO> comments;
    private String thumbnailImageUrl;  // 대표 이미지 경로 추가
    private String category;


}
