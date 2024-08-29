package com.red.domovie.domain.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeTheater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private LocalDateTime createdDate;

    private int viewCount;

    private int commentCount;

    private String content;

    private String thumbnailImageUrl; // 이미지 파일 이름

    @OneToMany(mappedBy = "homeTheater", cascade = CascadeType.ALL)
    private List<Comment> comments;
}