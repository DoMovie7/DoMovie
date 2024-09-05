package com.red.domovie.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final EmailServiceProcess emailService;

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
	public List<String> findEmailByNameAndBirthDate(FindIdDTO request) {
		return loginMapper.findEmailByNameAndBirthDate(request);
	}

    @Transactional
    public void processFindPassword(String email) {
        UserEntity user = loginMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("입력한 이메일과 일치하는 계정이 없습니다.");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        loginMapper.updateUser(user);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }


   

	@Override
	public void isValidPasswordResetToken(String token, Model model) {
		loginMapper.findByPasswordResetToken(token);
		model.addAttribute("token", token);
		
	}

	@Override
	public void resetPassword(String token, String newPassword, RedirectAttributes redirectAttributes) {
		UserEntity user = loginMapper.findByPasswordResetToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordResetToken(null);
            loginMapper.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
            logger.info("비밀번호 재설정 성공: {}", user.getEmail());
        } else {
            redirectAttributes.addFlashAttribute("error", "유효하지 않은 토큰입니다. 비밀번호 재설정을 다시 요청해주세요.");
            logger.warn("비밀번호 재설정 실패: 유효하지 않은 토큰 {}", token);
        }
		
		
	}

}