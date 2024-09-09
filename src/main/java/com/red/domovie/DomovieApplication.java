package com.red.domovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class DomovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomovieApplication.class, args);
	}
	
	
	  @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
