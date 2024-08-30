package com.red.domovie.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.UserEntityRepository;
import com.red.domovie.service.MypageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageServiceProcess implements MypageService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public ProfileDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();

        UserEntity user = userEntityRepository.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return ProfileDTO.from(user);
    }

	@Override
	@Transactional
	public void updateProcess(Long userId, ProfileUpdateDTO dto) {
		//userEntityRepository.findById(userId).orElseThrow().update(dto);
	}


}