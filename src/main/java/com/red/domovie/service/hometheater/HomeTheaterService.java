package com.red.domovie.service.hometheater;

import com.red.domovie.domain.dto.hometheater.*;
import com.red.domovie.domain.entity.hometheater.Comment;
import com.red.domovie.domain.entity.hometheater.HomeTheater;
import com.red.domovie.domain.repository.hometheater.CommentRepository;
import com.red.domovie.domain.repository.hometheater.HomeTheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeTheaterService {
    private final HomeTheaterRepository homeTheaterRepository;
    private final CommentRepository commentRepository;

    public List<HomeTheaterListDTO> getAllPosts() {
        return homeTheaterRepository.findAll().stream()
                .map(post -> new HomeTheaterListDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getAuthor(),
                        post.getCreatedDate(),
                        post.getViewCount(),
                        post.getCommentCount(),
                        post.getThumbnailImageUrl() // 썸네일 이미지 URL (필요한 경우 별도의 엔드포인트를 통해 이미지 데이터를 제공)
                ))
                .collect(Collectors.toList());
    }

    public HomeTheaterDetailDTO getPostById(Long id) {
        HomeTheater post = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        List<CommentDTO> comments = post.getComments().stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getAuthor(),
                        comment.getContent(),
                        comment.getCreatedDate()))
                .collect(Collectors.toList());

        return new HomeTheaterDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                post.getCreatedDate(),
                post.getViewCount(),
                post.getCommentCount(),
                post.getContent(),
                comments,
                post.getThumbnailImageUrl() // 썸네일 이미지 URL
        );
    }

    public void createPost(HomeTheaterSaveDTO homeTheaterSaveDTO, MultipartFile file) {
        HomeTheater homeTheater = new HomeTheater();
        homeTheater.setTitle(homeTheaterSaveDTO.getTitle());
        homeTheater.setContent(homeTheaterSaveDTO.getContent());
        homeTheater.setCreatedDate(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            saveImage(file, homeTheater);
        }

        homeTheaterRepository.save(homeTheater);
    }

    public HomeTheaterUpdateDTO getPostForUpdate(Long id) {
        HomeTheater post = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        return new HomeTheaterUpdateDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getThumbnailImageUrl()
        );
    }

    public void updatePost(Long id, HomeTheaterUpdateDTO postForm, MultipartFile file) {
        HomeTheater homeTheater = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        homeTheater.setTitle(postForm.getTitle());
        homeTheater.setContent(postForm.getContent());

        if (file != null && !file.isEmpty()) {
            saveImage(file, homeTheater);
        }

        homeTheaterRepository.save(homeTheater);
    }

    private void saveImage(MultipartFile file, HomeTheater homeTheater) {
        try {
            homeTheater.setThumbnailImage(file.getBytes());
            homeTheater.setThumbnailImageUrl(file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public void addComment(Long postId, CommentSaveDTO commentForm) {
        HomeTheater post = homeTheaterRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        Comment comment = new Comment();
        comment.setContent(commentForm.getContent());
        comment.setHomeTheater(post);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setAuthor("Anonymous"); // 실제로는 현재 로그인한 사용자의 정보를 사용해야 합니다

        commentRepository.save(comment);
    }
}