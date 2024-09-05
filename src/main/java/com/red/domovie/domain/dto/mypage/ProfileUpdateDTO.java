package com.red.domovie.domain.dto.mypage;

import com.red.domovie.domain.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileUpdateDTO {
	
	private long userId; // 유저 고유 ID
	private String nickName; // 닉네임
	private String currentPassword; // 현재 비밀번호
    private String newPassword; // 새로운 비밀번호
    private String birthDate; // 생년월일
    private String phoneNumber; // 핸드폰 번호
	private String profileImageUrl;
	
	// 이미지 관련 필드 추가
	private String profileImageBucketKey;
	private String profileImageOrgName;
	
	public UserEntity toUserEntity() {
		return UserEntity.builder()
				.userId(userId)
				.nickName(nickName)
				.birthDate(birthDate)
				.phoneNumber(phoneNumber)
				.build();
	}
	
}
