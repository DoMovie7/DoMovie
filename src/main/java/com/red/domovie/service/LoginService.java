package com.red.domovie.service;

import java.util.Map;

import com.red.domovie.domain.dto.login.FindIdDTO;
import com.red.domovie.domain.dto.login.SignUpDTO;

public interface LoginService {

	void saveProcess(SignUpDTO dto) throws Exception;

	Map<String, Boolean> checkEmailDuplication(String email);

	String findEmailByNameAndBirthDate(FindIdDTO request);

}
