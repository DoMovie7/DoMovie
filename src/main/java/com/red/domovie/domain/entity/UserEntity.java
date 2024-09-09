package com.red.domovie.domain.entity;


import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.enums.Tier;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;


@DynamicUpdate
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId; // 사용자ID

    @Column(nullable = false)
    private String userName; // 사용자이름

    @Column(nullable = false)
    private String nickName; // 닉네임

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String phoneNumber; // 핸드폰번호

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String birthDate; // 생년월일


    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "userId"))
    @Builder.Default
    @Column(name = "role")
    private Set<Role> roles = new HashSet<Role>(); // 'Role' Enum 타입을 별도로 정의
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Tier tier =Tier.CORN ;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SocialLoginEntity> socialLogins;

  //Role 등록하기 위한 편의 메서드 
  	public UserEntity addRole(Role role) {
  		roles.add(role);
  		return this;
  	}
  	@Column(name = "provider")
    private String provider;
  	
  	@Column(name = "social_id")
  	private String socialId;
  	
  	@Column(name = "password_reset_token")
    private String passwordResetToken; // 새로 추가된 비밀번호 재설정 토큰 필드
  	
  	@Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;
  	
  	public UserEntity update(ProfileUpdateDTO dto) {
  	    // DTO에서 닉네임을 가져와서 엔티티의 필드를 업데이트합니다.
  	    if (dto.getNickName() != null) {
  	        this.nickName = dto.getNickName();
  	    }
  	    // 필요한 경우 다른 필드들도 업데이트합니다.
  	    return this;
  	}
  	
  	private String profileImageUrl;
  	private String profileImagekey;
  	public UserEntity profileImageUpdate(String profileImageUrl, String profileImagekey) {
  		this.profileImageUrl=profileImageUrl;
  		this.profileImagekey=profileImagekey;
  		return UserEntity.this;
  	}
	


}