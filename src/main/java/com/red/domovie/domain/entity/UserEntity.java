package com.red.domovie.domain.entity;


import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId; // 사용자ID

    @Column(nullable = false)
    private String userName; // 사용자이름
    
    @Column(nullable = false)
    private String nickName; // 닉네임
    
    @Column(nullable = false,unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String phoneNumber; // 핸드폰번호

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String birthDate; // 생년월일

    @Column(nullable = true)
    private long status; // 회원상태
    
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "userId"))
    @Builder.Default
    @Column(name = "role")
    private Set<Role> roles = new HashSet<Role>(); // 'Role' Enum 타입을 별도로 정의
    
	public UserEntity addRole(Role role) {
		roles.add(role);
		return this;
	}
    
	public UserEntity addRoleByRange(String role) {
		for(int i=0; i<=Role.valueOf(role).ordinal(); i++) { //.ordinal() == 범위
			roles.add(Role.values()[i]); //addRole 메서드가 없는 경우
		}

		return this;
	}
}