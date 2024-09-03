package com.red.domovie.service;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;

public interface MypageService {

	ProfileDTO getCurrentUser();

	void updateProcess(Long userId, ProfileUpdateDTO dto);

}
