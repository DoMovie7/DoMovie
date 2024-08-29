package com.red.domovie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.UserEntityRepository;

@SpringBootTest
class DomovieApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserEntityRepository mRepository;
	
	//@Test
	void singIn() {
		// 일반 사용자 생성
        UserEntity user = UserEntity.builder()
                .userName("일반 사용자")
                .nickName("유저1")
                .email("user@test.com")
                .phoneNumber("01012345678")
                .password(passwordEncoder.encode("1234"))
                .birthDate("1990-01-01")
                .status(1L)
                .build()
                .addRole(Role.USER);

        mRepository.save(user);

        // 관리자 생성
        UserEntity admin = UserEntity.builder()
                .userName("관리자")
                .nickName("어드민1")
                .email("admin@test.com")
                .phoneNumber("01087654321")
                .password(passwordEncoder.encode("1234"))
                .birthDate("1985-12-31")
                .status(1L)
                .build()
                .addRole(Role.USER)
                .addRole(Role.ADMIN);

        mRepository.save(admin);

        System.out.println("테스트 데이터가 성공적으로 초기화되었습니다.");
	}
}
