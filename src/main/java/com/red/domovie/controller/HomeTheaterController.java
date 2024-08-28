package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hometheater")
public class HomeTheaterController {

    @GetMapping
    public String homeTheaterMain() {
        return "views/hometheater/hometheater";
    }

    @GetMapping("/list")
    public String listPosts(Model model) {
        List<PostDto> posts = getDummyPosts();
        model.addAttribute("posts", posts);
        return "views/hometheater/hometheater_list";
    }

    private List<PostDto> getDummyPosts() {
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostDto(1L, "홈시어터 스피커 추천", "user1", LocalDateTime.now(), 15, 3));
        posts.add(new PostDto(2L, "4K 프로젝터 사용 후기", "user2", LocalDateTime.now().minusDays(1), 32, 7));
        posts.add(new PostDto(3L, "서라운드 음향 설정 팁", "user3", LocalDateTime.now().minusDays(2), 28, 5));
        posts.add(new PostDto(4L, "홈시어터용 리시버 고르는 법", "user4", LocalDateTime.now().minusDays(3), 41, 9));
        posts.add(new PostDto(5L, "OLED vs QLED 비교", "user5", LocalDateTime.now().minusDays(4), 56, 12));
        return posts;
    }

// 간단한 DTO 클래스
private static class PostDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;

    // 생성자, getter, setter 생략 (IDE에서 자동 생성 가능)

    public PostDto(Long id, String title, String author, LocalDateTime createdAt, int viewCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    // getter 메서드들...
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getViewCount() { return viewCount; }
    public int getCommentCount() { return commentCount; }
}
}