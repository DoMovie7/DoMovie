package com.red.domovie.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.red.domovie.domain.dto.login.FindIdDTO;
import com.red.domovie.domain.dto.login.SignUpDTO;
import com.red.domovie.security.CustomUserDetails;

public interface LoginService {

	void saveProcess(SignUpDTO dto) throws Exception;

	Map<String, Boolean> checkEmailDuplication(String email);

	List<String> findEmailByNameAndBirthDate(FindIdDTO request);

	void processFindPassword(String email);

	void isValidPasswordResetToken(String token, Model model);

	void resetPassword(String token, String newPassword);




}

