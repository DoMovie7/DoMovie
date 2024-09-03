package com.red.domovie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.dto.recommend.RecommendListDTO;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.RecommendRepository;
import com.red.domovie.domain.repository.UserEntityRepository;
import com.red.domovie.service.MypageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageServiceProcess implements MypageService {

	private final UserEntityRepository userEntityRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final RecommendRepository recommendRepository;

	@Override
	public ProfileDTO getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		String username = authentication.getName();

		UserEntity user = userEntityRepository.findByEmail(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		int count=recommendRepository.countByAuthor(user);

		return modelMapper.map(user, ProfileDTO.class).recommendCount(count);
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

	@Override
	public List<RecommendListDTO> recommendsByUserProcess(Long userId) {
		UserEntity author = userEntityRepository.findById(userId).orElseThrow();
		
		return recommendRepository.findByAuthor(author).stream()
				.map(reommmend->modelMapper.map(reommmend, RecommendListDTO.class))
				.collect(Collectors.toList())
			;
		
	}

}