package com.red.domovie.domain.dto.hometheater;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HomeTheaterSaveDTO {
    private String title;
    private String content;
    private MultipartFile thumbnailImageUrl;  // 이미지 파일 추가
}