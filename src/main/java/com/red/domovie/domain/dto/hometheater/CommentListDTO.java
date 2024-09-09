package com.red.domovie.domain.dto.hometheater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentListDTO {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}