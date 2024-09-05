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

	//사용자가 넘긴 이메일로 이메일을 발송하기위한 로직
    @Transactional
    public void processFindPassword(String email) {
    	//유저 엔터티로 만든 이유: 
        UserEntity user = loginMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("입력한 이메일과 일치하는 계정이 없습니다.");
        }
        //임의로 만든 토큰값
        String resetToken = UUID.randomUUID().toString();
        
        
        user.setPasswordResetToken(resetToken);
        loginMapper.updateUser(user);
       
        
        //최종적으로 인증을 위한 이메일 발송 로직 
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }


   

	@Override
	public void isValidPasswordResetToken(String token, Model model) {
		loginMapper.findByPasswordResetToken(token);
		model.addAttribute("token", token);
		
	}

	@Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
		
		// 실제 넘어온 토큰값이 유저에 존재하는지 확인
	    UserEntity user = loginMapper.findByPasswordResetToken(token);
	    if (user == null) {
	        throw new RuntimeException("유효하지 않은 토큰입니다.");
	    }

	    // 넘어온 패스워드를 인코딩 함
	    String encodedPassword = passwordEncoder.encode(newPassword);
	    
	    System.out.println("Encoded password: " + encodedPassword);
	    System.out.println("Token: " + token);
	    
	    // 인코딩된 비밀번호와 토큰을 사용하여 업데이트
	    loginMapper.updatePassword(encodedPassword, token);
	    
	    logger.info("비밀번호 재설정 성공: {}", user.getEmail());
    }

}