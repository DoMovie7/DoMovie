package com.red.domovie.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.red.domovie.domain.dto.login.FindIdDTO;
import com.red.domovie.domain.dto.login.SignUpDTO;
import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.mapper.LoginMapper;
import com.red.domovie.security.CustomUserDetails;
import com.red.domovie.service.LoginService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceProcess implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceProcess.class);

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveProcess(SignUpDTO dto) throws Exception {
    	logger.info("회원가입 요청 받음: {}", dto);
    	
        UserEntity user = dto.toEntity(passwordEncoder);
        
    	System.out.println(user);
       
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        logger.debug("비밀번호 암호화 완료");

        // 사용자 정보 저장
        try {
            loginMapper.saveUser(user);
        } catch (Exception e) {
            logger.error("사용자 정보 저장 중 오류 발생", e);
            throw e;
        }

    }

    @Override
    public Map<String, Boolean> checkEmailDuplication(String email) {
        logger.info("이메일 중복 확인 요청: {}", email);
        Map<String, Boolean> response = new HashMap<>();
        try {
            boolean isDuplicate = loginMapper.countByEmail(email) > 0;
            response.put("isDuplicate", isDuplicate);
            logger.info("이메일 중복 확인 결과: {}", isDuplicate);
        } catch (Exception e) {
            logger.error("이메일 중복 확인 중 오류 발생", e);
            throw new RuntimeException("이메일 중복 확인 중 오류가 발생했습니다.", e);
        }
        return response;
    }

	@Override
	public String findEmailByNameAndBirthDate(FindIdDTO request) {
		return loginMapper.findEmailByNameAndBirthDate(request);
	}

}