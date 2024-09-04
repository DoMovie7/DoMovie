package com.red.domovie.service;

import java.util.List;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.dto.recommend.RecommendListDTO;

public interface MypageService {

	ProfileDTO getCurrentUser();

	void updateProcess(Long userId, ProfileUpdateDTO dto);

	List<RecommendListDTO> recommendsByUserProcess(Long userId);

}
