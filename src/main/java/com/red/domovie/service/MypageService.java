package com.red.domovie.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.dto.recommend.RecommendListDTO;

public interface MypageService {

	ProfileDTO getCurrentUser();

	void updateProcess(Long userId, ProfileUpdateDTO dto);

	List<RecommendListDTO> recommendsByUserProcess(Long userId);

	Map<String, String> profileImageUpdateProcess(Long userId, MultipartFile profile);

}
