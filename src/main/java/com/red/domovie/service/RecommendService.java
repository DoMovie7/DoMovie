package com.red.domovie.service;

import org.springframework.ui.Model;
import com.red.domovie.domain.dto.recommend.RecommendSaveDTO;
import com.red.domovie.domain.entity.RecommendEntity;

// RecommendService는 추천 목록과 관련된 비즈니스 로직을 처리하기 위한 서비스 계층의 인터페이스입니다.
// 이 인터페이스는 추천 목록의 조회, 저장, 관리와 같은 기능을 정의합니다.
public interface RecommendService {

    // 추천 목록을 처리하여 주어진 모델 객체에 추가하는 메서드입니다.
    // 이 메서드는 컨트롤러에서 호출되어, 추천 목록 데이터를 모델에 담아 뷰로 전달하는 역할을 합니다.
    // 매개변수로 전달되는 Model 객체는 뷰에서 사용할 데이터를 저장하는 데 사용됩니다.
    void listProcess(Model model);
    
    // 추천 글을 저장하는 메서드입니다.
    // RecommendSaveDTO 객체와 사용자 ID를 매개변수로 받아서, 해당 데이터를 사용하여 새로운 추천 글을 데이터베이스에 저장합니다.
    // RecommendSaveDTO는 저장할 추천 글의 제목, 내용, 장르 등의 정보를 포함합니다.
    // userId는 추천 글을 작성한 사용자를 식별하기 위해 사용됩니다.
    void savePost(RecommendSaveDTO dto, Long userId);
    
    // 특정 추천 글을 조회하는 메서드입니다.
    // 매개변수로 전달된 추천 글의 ID를 사용하여 해당 글을 데이터베이스에서 조회하고, RecommendEntity 객체로 반환합니다.
    // 이 메서드는 특정 글의 상세 정보를 조회할 때 사용됩니다.
	RecommendEntity getPost(Long id);

}