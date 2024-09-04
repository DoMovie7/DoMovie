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
    int countByEmail(String email);
    String findEmailByNameAndBirthDate(FindIdDTO findIdDTO);
    UserEntity findByEmail(@Param("email") String email);
    UserEntity findByPasswordResetToken(String resetToken);
    int updateUser(UserEntity user);
}
