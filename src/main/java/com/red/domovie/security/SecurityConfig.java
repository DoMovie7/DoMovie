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
	//private final CustomOAuth2UserService customOAuth2UserService;
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  	
        http
	        	//csrf 보호
            //.csrf(csrf->csrf.disable())
		    		.csrf(csrf -> csrf
    				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    				.ignoringRequestMatchers("/logout", "/api/check-email","/api/find-id") //csrf 보호 제외
    				)
        		
            
            //uri에 대한 보안
            .authorizeHttpRequests(authorize -> authorize
          	.requestMatchers("/mypage/**").authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN") //admin으로 시작하는 주소는 'ADMIN' 권한이 필요하다는 의미
            .requestMatchers("/api/check-email", "/api/find-id").permitAll()
            .anyRequest().permitAll() //위 설정을 제외한 나머지 요청은 인증 필요
            )
            
            //http 인증 설정 (기본 설정 사용)
            .httpBasic(Customizer.withDefaults())
            
            //form 로그인 설정 (기본 설정 사용)
            .formLogin(login -> login
          		  .loginPage("/signin") //로그인 페이지로 이동하는 url
          		  .failureUrl("/signin?error=true") //로그인 실패시 url
          		  .permitAll()
          		  .usernameParameter("email")
          		  .passwordParameter("password")
          		  .defaultSuccessUrl("/", true) 
          		  )
            
            
            //logout 설정
            .logout(logout -> logout
          		  .logoutSuccessUrl("/signin") //로그아웃 시 로그인 페이지로
          		  .invalidateHttpSession(true)
          		  .deleteCookies("JSESSIONID")) //로그아웃 시 쿠키 삭제
            //GET 요청을 통해 로그아웃을 처리하도록 허용
            .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")))
            
            .userDetailsService(customUserDetailsService);
        	/*// OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
            );*/
        return http.build();
    }	
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}


}
