package com.red.domovie.domain.entity.hometheater;

import com.red.domovie.domain.dto.hometheater.HomeTheaterUpdateDTO;
import com.red.domovie.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "home_theater")
public class HomeTheaterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    private LocalDateTime createdDate;

    private int viewCount;

    private int commentCount;

    private String content;

    private String thumbnailImageUrl;

    private String category;

    public HomeTheaterEntity update( HomeTheaterUpdateDTO updateDTO) {
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();
        return this;
    }

    @OneToMany(mappedBy = "homeTheater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntities;


    public boolean isAuthor(UserEntity user) {
        return this.author.getUserId() == user.getUserId();
    }

}