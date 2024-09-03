package com.red.domovie.service;

import org.springframework.ui.Model;

import com.red.domovie.domain.dto.recommend.RecommendSaveDTO;
import com.red.domovie.domain.entity.RecommendEntity;

// RecommendService는 추천 목록을 처리하기 위한 서비스 계층의 인터페이스입니다.
public interface RecommendService {

    // 추천 목록을 모델에 추가하는 메서드
    void listProcess(Model model);
    
    //글 저장
    void savePost(RecommendSaveDTO dto, Long userId);
    
    //글 조회
	RecommendEntity getPost(Long id);

}
