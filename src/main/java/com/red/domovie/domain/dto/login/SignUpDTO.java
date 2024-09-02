package com.red.domovie.domain.dto.login;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.TierEntity;
import com.red.domovie.domain.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpDTO {
	private String userName;
	private String nickName;
	private String email;
	private String phoneNumber;
	private String password;
	private String birthDate;
	private Role role;
	
	public UserEntity toEntity(PasswordEncoder pe) {
        // 기본 티어 생성
        TierEntity defaultTier = TierEntity.builder()
                .tierId(1L)
                .minPostCount(0)
                .maxPostCount(10)
                .build();

        UserEntity entity = UserEntity.builder()
                .userName(userName)
                .nickName(nickName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(pe.encode(password))
                .birthDate(birthDate)
                .tierId(defaultTier)  // 기본 티어 설정
                .build();

        entity.addRole(Role.USER);
        System.out.println(">>>>>>>>>" + entity);
        return entity;
    }
}