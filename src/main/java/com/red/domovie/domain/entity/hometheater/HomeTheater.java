package com.red.domovie.domain.entity.hometheater;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private String thumbnailImageUrl;


    @Lob
    @Column(name = "thumbnail_image", columnDefinition = "LONGBLOB")
    private byte[] thumbnailImage;

    @OneToMany(mappedBy = "homeTheater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 댓글 추가 메서드
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setHomeTheater(this);
    }

    // 댓글 제거 메서드
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setHomeTheater(null);
    }

}