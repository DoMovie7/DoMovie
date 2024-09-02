package com.red.domovie.domain.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.red.domovie.domain.dto.login.FindIdDTO;
import com.red.domovie.domain.dto.login.SignUpDTO;
import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.UserEntity;

@Mapper
public interface LoginMapper {
	void saveUser(UserEntity user);
    // 이메일 중복 체크를 위한 메소드 추가
	int countByEmail(String email);
	
	String findEmailByNameAndBirthDate(FindIdDTO request);
	
	
}
