package com.red.domovie.domain.dto.mypage;

import com.red.domovie.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

	private long userId; // 사용자ID
	private String userName; // 사용자이름
	private String birthDate; // 생년월일
	private String phoneNumber; // 핸드폰번호
	private String nickName; // 닉네임
	private String email; // 이메일
	
	// UserEntity를 ProfileDTO로 변환하는 메서드
    public static ProfileDTO from(UserEntity userEntity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setUserName(userEntity.getUserName());
        dto.setBirthDate(userEntity.getBirthDate());
        dto.setPhoneNumber(userEntity.getPhoneNumber());
        dto.setNickName(userEntity.getNickName());
        dto.setEmail(userEntity.getEmail());
        return dto;
    }
	
}
