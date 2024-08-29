package com.red.domovie.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.red.domovie.domain.entity.UserEntity;

import lombok.Getter;

@Getter
//principle 객체
public class CustomUserDetails extends User{

private static final long serialVersionUID = 1L; //1L : 시리얼 정보 // 닉네임을 추가하면 2버전이라고 2L로 수정할 것
	
	private String email; //=username
	private String userName; //한글이름
	
	public CustomUserDetails(UserEntity entity) {
		super(entity.getEmail(), entity.getPassword(),
				entity.getRoles().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role))
				.collect(Collectors.toSet()));
		email=entity.getEmail();
		userName=entity.getUserName();
	}
	
}
