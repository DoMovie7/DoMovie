package com.red.domovie.security;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.red.domovie.domain.entity.Role;
import com.red.domovie.domain.entity.TierEntity;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.TierEntityRepository;
import com.red.domovie.domain.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userRepository;
    private final TierEntityRepository tierRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("Social login attempt with provider: {}", registrationId);
        return processSocialLogin(oAuth2User, registrationId);
    }
    
    private OAuth2User processSocialLogin(OAuth2User oAuth2User, String registrationId) {
        String email = extractEmail(oAuth2User, registrationId);
        String name = extractName(oAuth2User, registrationId);
        UserEntity user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, name))
                .orElseGet(() -> createSocialUser(email, name));
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
    
    private String extractEmail(OAuth2User oAuth2User, String registrationId) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        switch (registrationId) {
            case "google":
                return (String) attributes.get("email");
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("email");
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                return (String) kakaoAccount.get("email");
            default:
                throw new OAuth2AuthenticationException("Unsupported registration ID: " + registrationId);
        }
    }
    
    private String extractName(OAuth2User oAuth2User, String registrationId) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        switch (registrationId) {
            case "google":
                return (String) attributes.get("name");
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("name");
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                return (String) profile.get("nickname");
            default:
                throw new OAuth2AuthenticationException("Unsupported registration ID: " + registrationId);
        }
    }
    
    private UserEntity createSocialUser(String email, String name) {
        log.info("Creating new social user: {}", email);
        TierEntity defaultTier = tierRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Default tier not found"));

        UserEntity entity = UserEntity.builder()
                .email(email)
                .userName(name)
                .nickName(name)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .phoneNumber("미입력")
                .birthDate("미입력")
                .tierId(defaultTier)
                .build()
                .addRole(Role.USER);

        return userRepository.save(entity);
    }

    private UserEntity updateExistingUser(UserEntity existingUser, String name) {
        log.info("Updating existing user: {}", existingUser.getEmail());
        existingUser.setUserName(name);
        return userRepository.save(existingUser);
    }
}