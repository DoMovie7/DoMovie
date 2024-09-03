package com.red.domovie.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;

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
		UserEntity user = userEntityRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

		// 닉네임 업데이트
		if (dto.getNickName() != null && !dto.getNickName().isEmpty()) {
			user.setNickName(dto.getNickName());
		}

		// 비밀번호 업데이트
		if (dto.getCurrentPassword() != null && dto.getNewPassword() != null) {
			if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
				throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
			}
			user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		}

		userEntityRepository.save(user);
	}

}