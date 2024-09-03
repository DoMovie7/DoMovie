package com.red.domovie.service.hometheater;

import com.red.domovie.domain.dto.hometheater.*;
import com.red.domovie.domain.entity.hometheater.Category;
import com.red.domovie.domain.entity.hometheater.CommentEntity;
import com.red.domovie.domain.entity.hometheater.HomeTheaterEntity;
import com.red.domovie.domain.repository.hometheater.CategoryRepository;
import com.red.domovie.domain.repository.hometheater.CommentRepository;
import com.red.domovie.domain.repository.hometheater.HomeTheaterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeTheaterService {
    private final CategoryRepository categoryRepository;
    private final HomeTheaterRepository homeTheaterRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<HomeTheaterListDTO> getAllPosts() {
        return homeTheaterRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, HomeTheaterListDTO.class))
                .collect(Collectors.toList());
    }

    public HomeTheaterDetailDTO getPostById(Long id) {
        HomeTheaterEntity entity = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        return modelMapper.map(entity, HomeTheaterDetailDTO.class);
    }

    public void createPost(HomeTheaterSaveDTO homeTheaterSaveDTO, MultipartFile file) {
        HomeTheaterEntity homeTheaterEntity = modelMapper.map(homeTheaterSaveDTO, HomeTheaterEntity.class);

        if (file != null && !file.isEmpty()) {
            try {
                String fileUrl = s3Service.uploadFile(file);
                homeTheaterEntity.setThumbnailImageUrl(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 중 오류 발생", e);
            }
        }

        homeTheaterRepository.save(homeTheaterEntity);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String path = "path/to/your/upload/directory/" + filename;
        file.transferTo(new java.io.File(path));
        return filename;
    }


    @Transactional
    public void updatePost(Long id, HomeTheaterUpdateDTO updateDTO) {
        homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"))
                .update(updateDTO);


        //homeTheaterRepository.save(homeTheaterEntity);
    }


    // 댓글 제거 메서드
    public void removeComment(CommentEntity commentEntity) {

    }


    public void addComment(Long homeTheaterId, CommentSaveDTO commentForm) {
        HomeTheaterEntity homeTheater = homeTheaterRepository.findById(homeTheaterId)
                .orElseThrow(() -> new EntityNotFoundException("Home Theater not found"));

        CommentEntity comment = CommentEntity.builder()
                .content(commentForm.getContent())
                .createdDate(LocalDateTime.now())
                .homeTheater(homeTheater) // homeTheaterEntity에서 homeTheater로 변경
                .build();

        commentRepository.save(comment);
    }
}





