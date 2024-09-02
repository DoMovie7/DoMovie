package com.red.domovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DomovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomovieApplication.class, args);
	}
	
	
	  @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
