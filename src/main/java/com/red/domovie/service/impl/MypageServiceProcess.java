package com.red.domovie.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.red.domovie.common.util.DomovieFileUtil;
import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.dto.recommend.RecommendListDTO;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.enums.Tier;
import com.red.domovie.domain.repository.RecommendRepository;
import com.red.domovie.domain.repository.UserEntityRepository;
import com.red.domovie.service.MypageService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class MypageServiceProcess implements MypageService {

	private final DomovieFileUtil fileUtil;
	
	private final UserEntityRepository userEntityRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final RecommendRepository recommendRepository;
	
	
	
	private final S3Client s3Client;
	
	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;
	
	@Value("${spring.cloud.aws.s3.upload-src.profile}")
	private String src;

	
	// 게시글 수 불러오기
	@Transactional
	@Override
	public ProfileDTO getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		String username = authentication.getName();

		// findByEmail(username) -> username 이메일과 일치하는 사용자를 조회 후 없으면 예외처리 ("User not found" 출력)
		UserEntity user = userEntityRepository.findByEmail(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		// user(로그인 된 회원)이 작성한 총 게시글 수를 변수 count에 저장
		int count=recommendRepository.countByAuthor(user);
		System.out.println("count:"+count);
		
		if(count<2 && !user.getTier().equals(Tier.CORN)) {
			user=user.tierUpdate(Tier.CORN);
		}else if(count<4 && !user.getTier().equals(Tier.POPCORN)) {
			user=user.tierUpdate(Tier.POPCORN);
		}else if(!user.getTier().equals(Tier.FULLPOPCORN)){
			user=user.tierUpdate(Tier.FULLPOPCORN);
		}
		

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

	
	// 게시글 불러오기
	@Override
	public List<RecommendListDTO> recommendsByUserProcess(Long userId) {
		// userId를 이용해 해당 사용자를 조회합니다. 
    // 사용자가 존재하지 않을 경우 예외를 던집니다.
		UserEntity author = userEntityRepository.findById(userId).orElseThrow();
		
		// 조회된 사용자를 작성자로 하는 추천 글 목록을 조회합니다.
		// 조회된 추천 글들을 RecommendListDTO로 변환한 후, 리스트로 수집하여 반환합니다.
		return recommendRepository.findByAuthor(author).stream()
				.map(reommmend->modelMapper.map(reommmend, RecommendListDTO.class))
				.collect(Collectors.toList());
			
		
	}

	@Transactional
	@Override
	public Map<String, String> profileImageUpdateProcess(Long userId, MultipartFile profile) {
		
		//String newName=fileUtil.newFilenameWithoutExtension();
		String key=src+fileUtil.newFilenameWithoutExtension();
		//String orgName=profile.getOriginalFilename();
		//s3 src폴더에 upload
		Map<String, String> result=fileUtil.awsS3fileUpload(profile, s3Client, bucket, key);
		//DB수정
		UserEntity user=userEntityRepository.findById(userId).orElseThrow();
		String prevImageKey=user.getProfileImagekey();
		//수정
		user.profileImageUpdate(result.get("url"), key);
		if(prevImageKey!=null && prevImageKey.equals(""))
			fileUtil.awsS3DeleteObject(s3Client, bucket, prevImageKey);
		
		return result;
	}

	@Transactional
	@Override
	public Map<String, Object> getTierProcess(Long userId) {
		// findByEmail(username) -> username 이메일과 일치하는 사용자를 조회 후 없으면 예외처리 ("User not found" 출력)
		UserEntity user = userEntityRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		// user(로그인 된 회원)이 작성한 총 게시글 수를 변수 count에 저장
		int count=recommendRepository.countByAuthor(user);
		if(count<2 && !user.getTier().equals(Tier.CORN)) {
			user=user.tierUpdate(Tier.CORN);
		}else if(count<4 && !user.getTier().equals(Tier.POPCORN)) {
			user=user.tierUpdate(Tier.POPCORN);
		}else if(!user.getTier().equals(Tier.FULLPOPCORN)){
			user=user.tierUpdate(Tier.FULLPOPCORN);
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("recommendCount", count);
	    response.put("tierName", user.getTier().name());
	    response.put("desc", user.getTier().desc());
	    response.put("url", user.getTier().url());
		return response;
	}

}