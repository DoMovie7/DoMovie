package com.red.domovie.domain.dto.hometheater;

import com.red.domovie.domain.entity.hometheater.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HomeTheaterSaveDTO {
    private String title;
    private String content;
    private Category.CategoryType category;
    //private MultipartFile thumbnailImageUrl;  // 이미지 파일 추가
}