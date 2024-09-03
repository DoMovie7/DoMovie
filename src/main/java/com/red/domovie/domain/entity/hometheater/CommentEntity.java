package com.red.domovie.domain.entity.hometheater;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.red.domovie.domain.entity.BaseEntity;

import java.time.LocalDateTime;


@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, length = 50)
    private String author;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_theater_id", nullable = false)
    private HomeTheaterEntity homeTheater;

    public void update(String content) {
        this.content = content;
    }
}