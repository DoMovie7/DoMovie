package com.red.domovie.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.red.domovie.common.util.DomovieFileUtil;
import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.recommend.RecommendDetailDTO;
import com.red.domovie.domain.dto.recommend.RecommendFileSaveDTO;
import com.red.domovie.domain.dto.recommend.RecommendListDTO;
import com.red.domovie.domain.dto.recommend.RecommendSaveDTO;
import com.red.domovie.domain.dto.recommend.RecommendUpdateDTO;
import com.red.domovie.domain.entity.Genre;
import com.red.domovie.domain.entity.RecommendEntity;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.RecommendRepository;
import com.red.domovie.service.RecommendService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;

//@Component 서비스는 빈으로 스프링컨테이너에 보관 객체를만들어서, 싱글톤으로 관리.
//매번 서비스를 실행할때마다 new RecommendServiceProcess() 만드면 처리속도 저하
@Service
@RequiredArgsConstructor
public class RecommendServiceProcess implements RecommendService {
	
	private final RecommendRepository recommendRepository;	 
	private final ModelMapper modelMapper;
	private final S3Client s3Client;
	private final DomovieFileUtil domovieFileUtil;
	
	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;
	
	@Value("${spring.cloud.aws.s3.upload-temp.profile}")
	private String temp;
	
	@Value("${spring.cloud.aws.s3.upload-src.profile}")
	private String src;
	
	/* 장르별 리스트 처리 */
	@Override
	public void listProcess(Model model, int genreIdx) {
		int idx=genreIdx-1;
		if(idx==-1) {
			listProcess(model);
			return;
		}
		
		Genre genre=Genre.values()[idx];
		System.out.println(genre);
		
		List<RecommendListDTO> recommends =recommendRepository.findByGenre(genre).stream()
				.map(ent->{
	        		ProfileDTO author=modelMapper.map(ent.getAuthor(), ProfileDTO.class);
	        		return modelMapper.map(ent, RecommendListDTO.class).author(author);
        		})
				.collect(Collectors.toList());
		model.addAttribute("list", recommends);
	}
    // RecommendService 인터페이스를 구현한 listProcess 메서드
    @Override
    public void listProcess(Model model) {
    	 // 데이터베이스에서 실제 추천 글 목록을 가져옴
        List<RecommendEntity> recommends = recommendRepository.findAll();
        

        // 데이터베이스에서 가져온 RecommendEntity 객체를 RecommendListDTO로 변환
        List<RecommendListDTO> dtoList=recommends.stream()
        	.map(ent->{
        		ProfileDTO author=modelMapper.map(ent.getAuthor(), ProfileDTO.class);
        		return modelMapper.map(ent, RecommendListDTO.class).author(author);
        		})
        	.collect(Collectors.toList());

        // 모델에 "list"라는 이름으로 생성한 리스트를 추가 -> view(thymeleaf - html)에서 "${list}" 접근 가능
        model.addAttribute("list", dtoList);
    }

    @Override
    public void savePost(RecommendSaveDTO dto, Long userId) {
    	//1.temp->src 폴더로 이동
    	//S3Client s3Client;
    	//String sourceBucket;
    	String sourceKey=dto.getBucketKey();
    	//String destinationBucket;
    	String destinationKey=src+dto.getNewName();
    	
    	//1.copy
    	String imgUrl=domovieFileUtil.awsS3CopyObject(s3Client, bucket, sourceKey, bucket, destinationKey);
    	//2.tempKey 삭제
    	domovieFileUtil.awsS3DeleteObject(s3Client, bucket, sourceKey);
    	RecommendEntity entity=modelMapper.map(dto.update(imgUrl,destinationKey), RecommendEntity.class);
    	//작성자
        recommendRepository.save(entity.author(UserEntity.builder().userId(userId).build()));
    }

    @Override
    public void getPost(Long id, Model model) {
    	model.addAttribute("recommend", recommendRepository.findById(id)
    			.map(ent->modelMapper.map(ent, RecommendDetailDTO.class))
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id)));
    }

	@Override
	public Map<String, String> tempUploadProcss(MultipartFile posterfile) {
		// temp 업로드를 위해
		String newName = domovieFileUtil.newFilenameWithoutExtension();
		String tempkey = temp + newName;
		String orgName = posterfile.getOriginalFilename();
		//MultipartFile, s3Client,bucket,tempkey 순서유지
		Map<String, String> result=domovieFileUtil.awsS3fileUpload(posterfile, s3Client, bucket, tempkey);
		result.put("newName", newName);
		result.put("orgName", orgName);
		return result;
	}



	@Override
	public void deletePost(long id) {
		RecommendEntity result=recommendRepository.findById(id).orElseThrow();
		recommendRepository.delete(result);
		//s3 이미지 삭제
		domovieFileUtil.awsS3DeleteObject(s3Client, bucket, result.getBucketKey());
		
	}

	@Override
	@Transactional
	public void updateProcess(Long id, RecommendUpdateDTO dto) {
		recommendRepository.findById(id)
			.map(entity->entity.updateGenreOrTitleOrContent(dto))
			.orElseThrow();
		
	}
	
	
	
}