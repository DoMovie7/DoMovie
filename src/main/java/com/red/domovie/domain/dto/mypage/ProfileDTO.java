package com.red.domovie.domain.dto.mypage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.red.domovie.domain.enums.Tier;

import lombok.Getter;
import lombok.ToString;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Getter
@ToString
public class ProfileDTO {

	private long userId; // 사용자ID
	private String userName; // 사용자이름
	private String birthDate; // 생년월일
	private String phoneNumber; // 핸드폰번호
	private String nickName; // 닉네임
	private String email; // 이메일
	private Tier tier; // 등급
	private String profileImageUrl; // 이미지 url
	
	private int  recommendCount;
	
	public ProfileDTO recommendCount(int  recommendCount) {
		this.recommendCount=recommendCount;
		return ProfileDTO.this;
	}
	
}
