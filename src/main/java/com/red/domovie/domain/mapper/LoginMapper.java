package com.red.domovie.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.red.domovie.domain.dto.SignUpDTO;
import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.UserEntity;

@Mapper
public interface LoginMapper {
	void saveUser(UserEntity user);
    // 이메일 중복 체크를 위한 메소드 추가
}
