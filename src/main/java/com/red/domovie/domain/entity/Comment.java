package com.red.domovie.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    private String content;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "home_theater_id")
    private HomeTheater homeTheater;
}