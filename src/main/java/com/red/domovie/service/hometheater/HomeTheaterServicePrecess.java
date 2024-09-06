package com.red.domovie.service.hometheater;

import com.red.domovie.common.util.DomovieFileUtil;
import com.red.domovie.domain.dto.hometheater.*;
import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.entity.hometheater.Category;
import com.red.domovie.domain.entity.hometheater.CommentEntity;
import com.red.domovie.domain.entity.hometheater.HomeTheaterEntity;
import com.red.domovie.domain.entity.hometheater.ItemImageEntity;
import com.red.domovie.domain.repository.UserEntityRepository;
import com.red.domovie.domain.repository.hometheater.CategoryRepository;
import com.red.domovie.domain.repository.hometheater.CommentRepository;
import com.red.domovie.domain.repository.hometheater.HomeTheaterRepository;
import com.red.domovie.domain.repository.hometheater.ItemImageEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeTheaterServicePrecess {

    private final CategoryRepository categoryRepository;
    private final HomeTheaterRepository homeTheaterRepository;
    private final CommentRepository commentRepository;
    private final UserEntityRepository userRepository;
    private final ItemImageEntityRepository itemImageRepository;

    private final ModelMapper modelMapper;

    private final S3Client s3Client;
    private final DomovieFileUtil domovieFileUtil;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${spring.cloud.aws.s3.upload-temp.theater}")
    private String temp;
    @Value("${spring.cloud.aws.s3.upload-src.theater}")
    private String src;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<HomeTheaterListDTO> getAllPosts() {
        return homeTheaterRepository.findByOrderByIdDesc().stream()
                .map(theater -> {
                    ItemImageEntity mainItemImage= itemImageRepository.findByHomeTheater(theater).stream()
                            .filter(image->image.isDefault()) // isDefault가 true인 이미지객체만통과
                            .findFirst().orElse(null);//만약에 여러장이면 첫번째거만
                    return modelMapper.map(theater, HomeTheaterListDTO.class)
                            .thumbnailImageUrl(mainItemImage!=null?mainItemImage.getImageUrl():"/img/index/no-movie-img.jpg");
                })
                .collect(Collectors.toList());
    }
    private HomeTheaterListDTO convertToDto(HomeTheaterEntity entity) {
        HomeTheaterListDTO dto = modelMapper.map(entity, HomeTheaterListDTO.class);
        dto.setAuthor(entity.getAuthorNickname()); // author 필드에 닉네임 설정
        return dto;
    }


    public HomeTheaterDetailDTO getPostById(Long id) {
        HomeTheaterEntity entity = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        HomeTheaterDetailDTO dto = modelMapper.map(entity, HomeTheaterDetailDTO.class);
        dto.setAuthorEmail(entity.getAuthor().getEmail());  // 작성자의 이메일 설정
        return modelMapper.map(entity, HomeTheaterDetailDTO.class);
    }
    @Transactional
    public boolean deletePost(Long id, String email) {
        HomeTheaterEntity post = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        if (post.isAuthor(user) || user.getRoles().contains(Role.ADMIN)) {
            homeTheaterRepository.delete(post);
            return true;
        }
        return false;
    }

    @Transactional
    public void createPost(HomeTheaterSaveDTO homeTheaterSaveDTO,ItemImageSaveDTO itemImageSaveDTO, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        HomeTheaterEntity homeTheaterEntity = HomeTheaterEntity.builder()
                .title(homeTheaterSaveDTO.getTitle())
                .content(homeTheaterSaveDTO.getContent())
                .author(user)
                .build();

        HomeTheaterEntity result=homeTheaterRepository.save(homeTheaterEntity);

        itemImageSaveDTO.toImageSaveListDTO().forEach(imageDto->{
            //temp->src 이동
            String tempKey=imageDto.getBucketKey();
            String srcKey=src+imageDto.getNewName();
            //1. s3에서 이미지 복사
            String imageUrl=domovieFileUtil.awsS3CopyObject(s3Client, bucket, tempKey, bucket, srcKey);
            //2. temp폴더 이미지는 삭제
            domovieFileUtil.awsS3DeleteObject(s3Client, bucket, tempKey);

            itemImageRepository.save(ItemImageEntity.builder()
                            .imageUrl(imageUrl)//copy후 획득
                            .bucketKey(srcKey)//이동시킨 버킷
                            .orgName(imageDto.getOrgName())
                            .newName(imageDto.getNewName())
                            .isDefault(imageDto.isDefault())
                            .homeTheater(result)//fk등록
                    .build());
        });

    }


    private String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String path = "path/to/your/upload/directory/" + filename;
        file.transferTo(new java.io.File(path));
        return filename;
    }


    @Transactional
    public HomeTheaterDetailDTO updatePost(Long id, HomeTheaterUpdateDTO updateDTO, UserDetails userDetails) {
        HomeTheaterEntity post = homeTheaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        UserEntity currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (post.isAuthor(currentUser)) {
            post.update(updateDTO);
            return modelMapper.map(post, HomeTheaterDetailDTO.class);
        }
        return null;
    }


    // 댓글 제거 메서드
    public void removeComment(CommentEntity commentEntity) {

    }


    public void addComment(Long homeTheaterId, CommentSaveDTO commentForm) {
        HomeTheaterEntity homeTheater = homeTheaterRepository.findById(homeTheaterId)
                .orElseThrow(() -> new EntityNotFoundException("Home Theater not found"));

        CommentEntity comment = CommentEntity.builder()
                .content(commentForm.getContent())
                .author(commentForm.getAuthor())
                .homeTheater(homeTheater) // homeTheaterEntity에서 homeTheater로 변경
                .build();

        commentRepository.save(comment);
    }

    public Map<String, String> tempUploadProcess(MultipartFile itemImage) {
        String newName =domovieFileUtil.newFilenameWithoutExtension();
        String tempkey=temp+newName;
        String orgName=itemImage.getOriginalFilename();
        System.out.println("bucket:"+bucket);
        System.out.println("temp:"+temp);
        System.out.println(orgName);
        Map<String, String> result=domovieFileUtil.awsS3fileUpload(itemImage,s3Client,bucket,tempkey);
        result.put("orgName",orgName);
        result.put("newName",newName);
        return result;
    }


}





