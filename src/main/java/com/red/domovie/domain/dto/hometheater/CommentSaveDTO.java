package com.red.domovie.domain.dto.hometheater;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentSaveDTO {
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
