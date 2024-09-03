package com.red.domovie.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 설정
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/logout", "/api/check-email", "/api/find-id")
            )

            // URI에 대한 보안 설정
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/mypage/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/check-email", "/api/find-id").permitAll()
                    .requestMatchers("/hometheater/hometheater_create", "/hometheater/create").authenticated()

                .anyRequest().permitAll()
            )

            // HTTP 기본 인증 설정
            .httpBasic(Customizer.withDefaults())

            // 폼 로그인 설정
            .formLogin(login -> login
                .loginPage("/signin")
                .failureUrl("/signin?error=true")
                .permitAll()
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
            )

            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 URL 설정
                .logoutSuccessUrl("/signin") // 로그아웃 성공 시 이동할 URL
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // POST 방식으로 설정
                .permitAll() // 로그아웃 요청 모두 허용
            )

            // UserDetailsService 설정
            .userDetailsService(customUserDetailsService)

            // OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/signin")
                .defaultSuccessUrl("/", true)
                .failureUrl("/signin?error=true")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .authorizationEndpoint(authorization -> authorization
                    .baseUri("/oauth2/authorization")
                )
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/login/oauth2/code/*")
                )
            );

        return http.build();
    }
}
