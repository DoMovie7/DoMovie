package com.red.domovie.service;

import com.red.domovie.domain.dto.*;
import com.red.domovie.domain.entity.HomeTheater;
import com.red.domovie.domain.repository.HomeTheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeTheaterService {
    private final HomeTheaterRepository homeTheaterRepository;

    // 이미지 저장 경로
    private final String imageUploadDir = "src/main/resources/static/img/upload/";

    public List<HomeTheaterListDTO> getAllPosts() {
        return homeTheaterRepository.findAll().stream()
                .map(post -> new HomeTheaterListDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getAuthor(),
                        post.getCreatedDate(),
                        post.getViewCount(),
                        post.getCommentCount(),
                        post.getThumbnailImageUrl() // 썸네일 이미지 추가
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
                post.getId(),                // id
                post.getTitle(),             // title
                post.getAuthor(),            // author
                post.getCreatedDate(),       // createdDate
                post.getViewCount(),         // viewCount
                post.getCommentCount(),      // commentCount
                post.getContent(),           // content
                comments,                    // comments
                post.getThumbnailImageUrl()  // thumbnailImageUrl
        );
    }

    public void createPost(HomeTheaterSaveDTO homeTheaterSaveDTO, MultipartFile file) {
        HomeTheater homeTheater = new HomeTheater();
        homeTheater.setTitle(homeTheaterSaveDTO.getTitle());
        homeTheater.setContent(homeTheaterSaveDTO.getContent());
        homeTheater.setCreatedDate(LocalDateTime.now());

        // 이미지 저장 처리
        if (file != null && !file.isEmpty()) {
            saveImage(file, homeTheater); // 이미지 저장
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
                post.getThumbnailImageUrl()  // 썸네일 이미지 추가
        );
    }

    public void updatePost(Long id, HomeTheaterUpdateDTO postForm, MultipartFile file) {
        HomeTheater homeTheater = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        homeTheater.setTitle(postForm.getTitle());
        homeTheater.setContent(postForm.getContent());

        // 이미지가 업로드되면 새 이미지로 변경
        if (file != null && !file.isEmpty()) {
            saveImage(file, homeTheater); // 이미지 저장
        }

        homeTheaterRepository.save(homeTheater);
    }

    private void saveImage(MultipartFile file, HomeTheater homeTheater) {
        try {
            // 저장할 파일의 경로 및 이름
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(imageUploadDir + fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), path);
            homeTheater.setThumbnailImageUrl(fileName); // 이미지 파일 이름 설정
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}