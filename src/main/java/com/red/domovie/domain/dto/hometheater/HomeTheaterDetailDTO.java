package com.red.domovie.domain.dto.hometheater;

import com.red.domovie.domain.entity.hometheater.ItemImageEntity;
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
    private String authorEmail;  // 새로 추가
    private List<ImageDetailDTO> images;

    public String getThumbnailImageUrl() {
        ImageDetailDTO mainItemImage= images.stream()
                .filter(image->image.isDefault()) // isDefault가 true인 이미지객체만통과
                .findFirst().orElse(ImageDetailDTO.builder().imageUrl("/img/index/no-movie-img.jpg").build());
        return mainItemImage.getImageUrl();
    }

}
