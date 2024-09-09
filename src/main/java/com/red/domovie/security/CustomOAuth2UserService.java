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
import com.red.domovie.domain.entity.SocialLoginEntity;
import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.repository.SocialLoginEntityRepository;
import com.red.domovie.domain.repository.UserEntityRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userRepository;
    private final SocialLoginEntityRepository socialLoginyRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("Social login attempt with provider: {}", registrationId);
        return processSocialLogin(oAuth2User, registrationId);
    }
    
    @Transactional
    private OAuth2User processSocialLogin(OAuth2User oAuth2User, String registrationId) {
        String email = extractEmail(oAuth2User, registrationId);
        System.out.println("social-email:"+email);
        String name = extractName(oAuth2User, registrationId);
        String socialId = extractSocialId(oAuth2User, registrationId);
        
        UserEntity user=userRepository.findByEmail(email).orElse(null);
        
        if(user!=null) {
        	System.out.println(">>>기존");
        	user=user.provider(registrationId)
        			.addSocial(SocialLoginEntity.builder()
        			.provider(registrationId)
        			.providerId(socialId)
        			.nickName(name)
        			.build());
        	userRepository.save(user);
        }else {
        	user=createSocialUser(email, name, registrationId, socialId);
        }
        
        /*
        UserEntity user = userRepository.findByEmailOrSocialId(email, socialId)
                .map(existingUser -> updateExistingUser(existingUser, name, registrationId, socialId))
                .orElseGet(() -> createSocialUser(email, name, registrationId, socialId));
        */
        
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
    
    private UserEntity updateExistingUser(UserEntity existingUser, String name, String provider, String socialId) {
        log.info("Updating existing user: {} with provider: {}", existingUser.getEmail(), provider);
        existingUser.setUserName(name);
        if (existingUser.getProvider() == null || existingUser.getProvider().isEmpty()) {
            existingUser.setProvider(provider);
            //existingUser.setSocialId(socialId);
        } else if (!existingUser.getProvider().equals(provider)) {
            log.warn("User {} already exists with different provider: {}", existingUser.getEmail(), existingUser.getProvider());
            // 여기서 필요한 경우 추가적인 처리를 할 수 있습니다.
        }
        return userRepository.save(existingUser);
    }
    
    
    private String extractName(OAuth2User oAuth2User, String registrationId) {
    	Map<String, Object> attributes = oAuth2User.getAttributes();
        String name = null;
        switch (registrationId) {
            case "google":
                name = (String) attributes.get("name");
                break;
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                name = (String) response.get("name");
                break;
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                name = (String) profile.get("nickname");
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported registration ID: " + registrationId);
        }
        
        if (name == null || name.isEmpty()) {
            log.warn("Name not provided by {}. Using default name.", registrationId);
            name = "User_" + UUID.randomUUID().toString().substring(0, 8);
        }
        
        return name;
	}

	private String extractEmail(OAuth2User oAuth2User, String registrationId) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println(">>>>>");
		for(String attributeName:attributes.keySet()) {
			System.out.print(attributeName+":");
			System.out.println(attributes.get(attributeName));
		}
		System.out.println("<<<<<");
        System.out.println("idididid-->"+registrationId);
        String email = null;
        switch (registrationId) {
            case "google":
                email = (String) attributes.get("email");
                break;
            case "naver":
            	
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                email = (String) response.get("email");
                break;
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                email = (String) kakaoAccount.get("email");
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported registration ID: " + registrationId);
        }
        return email;
    }
    
    private String extractSocialId(OAuth2User oAuth2User, String registrationId) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String id = null;
        switch (registrationId) {
            case "google":
                id = (String) attributes.get("sub");
                break;
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                id = (String) response.get("id");
                break;
            case "kakao":
                id = String.valueOf(attributes.get("id"));
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported registration ID: " + registrationId);
        }
        return registrationId + "_" + id;
    }
    
    // extractName 메서드는 그대로 유지

    private UserEntity createSocialUser(String email, String name, String provider, String socialId) {
        log.info("Creating new social user: {} with provider: {}", email, provider);

        System.out.println(">>>신규");
        UserEntity entity = UserEntity.builder()
                .email(email != null ? email : socialId)
                .userName(name)
                .nickName(name)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .phoneNumber("미입력")
                .birthDate("미입력")
                .provider(provider)
                .build()
                .addRole(Role.USER)
                .addSocial(SocialLoginEntity.builder()
            			.provider(provider)
            			.providerId(socialId)
            			.nickName(name)
            			.build())
                ;

        return userRepository.save(entity);
    }

    
}