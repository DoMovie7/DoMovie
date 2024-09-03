package com.red.domovie.domain.dto.mypage;

import com.red.domovie.domain.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileUpdateDTO {
	
	private long userId;
	private String nickName;
	private String profileImageUrl;
	
	// 이미지 관련 필드 추가
	private String profileImageBucketKey;
	private String profileImageOrgName;
	
	public UserEntity toUserEntity() {
		return UserEntity.builder()
				.userId(userId)
				.nickName(nickName)
				//.profileImageUrl(profileImageUrl)
				.build();
	}
	
}
