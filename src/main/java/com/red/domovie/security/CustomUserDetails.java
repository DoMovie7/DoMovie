package com.red.domovie.security;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.red.domovie.domain.entity.UserEntity;

import lombok.Getter;
import lombok.ToString;

@Getter
//principle 객체
@ToString
public class CustomUserDetails extends User implements OAuth2User {
    private static final long serialVersionUID = 2L; // 닉네임 추가로 2L로 수정

    private final Long userId;
    private final String email; // =username
    private final String userName; // 한글이름
    private final String nickName; // 추가된 닉네임
    private Map<String, Object> attributes;

    public CustomUserDetails(UserEntity entity) {
        super(entity.getEmail(), entity.getPassword(), entity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet()));

        this.userId = entity.getUserId();
        this.email = entity.getEmail();
        this.userName = entity.getUserName();
        this.nickName = entity.getNickName();
    }

    // OAuth2User를 위한 생성자
    public CustomUserDetails(UserEntity entity, Map<String, Object> attributes) {
        this(entity);
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return this.email;
    }
}
