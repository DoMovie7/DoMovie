package com.red.domovie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.TierEntity;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.TierEntityRepository;
import com.red.domovie.domain.repository.UserEntityRepository;

@SpringBootTest
class DomovieApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserEntityRepository mRepository;
	
	@Autowired
	TierEntityRepository tRepository;
	
	
	//@Test
	void signIn() {
	    // 기본 티어 생성 (ID: 1)
	    TierEntity defaultTier = TierEntity.builder()
	        .minPostCount(0)
	        .maxPostCount(10)
	        .build();
	    tRepository.save(defaultTier);
	    
	    // 일반 사용자 생성
	    UserEntity user = UserEntity.builder()
	        .userName("일반 사용자")
	        .nickName("유저1")
	        .email("user@test.com")
	        .phoneNumber("01012345678")
	        .password(passwordEncoder.encode("1234"))
	        .birthDate("1990-01-01")
	        //.status(1L)
	        .tierId(defaultTier)  // 기본 티어 설정
	        .build()
	        .addRole(Role.USER);

	    // 저장 및 검증 로직
	    mRepository.save(user);
	    
        System.out.println("테스트 데이터가 성공적으로 초기화되었습니다.");
	}
}
