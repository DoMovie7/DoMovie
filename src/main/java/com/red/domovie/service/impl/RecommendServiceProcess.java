package com.red.domovie.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.red.domovie.domain.dto.recommend.RecommendListDTO;
import com.red.domovie.domain.dto.recommend.RecommendSaveDTO;
import com.red.domovie.domain.entity.RecommendEntity;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.RecommendRepository;
import com.red.domovie.service.RecommendService;

import lombok.RequiredArgsConstructor;

//@Component 서비스는 빈으로 스프링컨테이너에 보관 객체를만들어서, 싱글톤으로 관리.
//매번 서비스를 실행할때마다 new RecommendServiceProcess() 만드면 처리속도 저하
@Service
@RequiredArgsConstructor
public class RecommendServiceProcess implements RecommendService {
	
	private final RecommendRepository recommendRepository;	 
	private final ModelMapper modelMapper;
	
    // RecommendService 인터페이스를 구현한 listProcess 메서드
    @Override
    public void listProcess(Model model) {
    	 // 데이터베이스에서 실제 추천 글 목록을 가져옴
        List<RecommendEntity> recommends = recommendRepository.findAll();
        
        List<RecommendListDTO> dtoList = new ArrayList<>();

        // 데이터베이스에서 가져온 RecommendEntity 객체를 RecommendListDTO로 변환
        for (RecommendEntity recommend : recommends) {
            dtoList.add(RecommendListDTO.builder()
                    .id(recommend.getId())  // 항목 번호
                    .title(recommend.getTitle())  // 항목 제목
                    .email(recommend.getAuthor().getEmail())  // 엔티티의 author 필드를 user로 매핑
        	        .userName(recommend.getAuthor().getUserName())  // 엔티티의 author 필드를 user로 매핑
                    .imgUrl(recommend.getImgUrl())  // 이미지 URL (필요시 추가)
                    .commentCount(recommend.getCommentCount())  // 댓글 수
                    .build());  // RecommendListDTO 객체 생성 및 리스트에 추가
        }

        // 모델에 "list"라는 이름으로 생성한 리스트를 추가 -> view(thymeleaf - html)에서 "${list}" 접근 가능
        model.addAttribute("list", dtoList);
    }

    @Override
    public void savePost(RecommendSaveDTO dto, Long userId) {
    	RecommendEntity entity=modelMapper.map(dto, RecommendEntity.class);
    	//작성자
        recommendRepository.save(entity.author(UserEntity.builder().userId(userId).build()));
    }

    @Override
    public RecommendEntity getPost(Long id) {
        return recommendRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }
}