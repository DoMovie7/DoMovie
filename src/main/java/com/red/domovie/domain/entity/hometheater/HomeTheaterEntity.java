package com.red.domovie.domain.entity.hometheater;

import com.red.domovie.domain.dto.hometheater.HomeTheaterUpdateDTO;
import com.red.domovie.domain.entity.BaseEntity;
import com.red.domovie.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "home_theater")
public class HomeTheaterEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;


    private int viewCount;

    private int commentCount;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String thumbnailImageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public HomeTheaterEntity update( HomeTheaterUpdateDTO updateDTO) {
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();
        return this;
    }

    @OneToMany(mappedBy = "homeTheater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntities;

    //////////////////////////////
    @OneToMany(mappedBy = "homeTheater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImageEntity> images = new ArrayList<>();
    ////////////////////////////

    public boolean isAuthor(UserEntity user) {
        return this.author.getUserId() == user.getUserId();
    }
    public String getAuthorNickname() {
        return author != null ? author.getNickName() : null;
    }


}