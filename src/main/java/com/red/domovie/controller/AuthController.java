package com.red.domovie.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.security.CustomUserDetails;

@RestController
public class AuthController {
	
    @GetMapping("/api/user/status")
    public ResponseEntity<?> getUserStatus() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
        if (authentication != null && authentication.isAuthenticated() && 
                !(authentication.getPrincipal() instanceof String)) {
        	
        	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok().body(Map.of("status", "authenticated", "userId", userDetails.getUserId(), "userName", userDetails.getUserName()));
        } else {
            return ResponseEntity.ok().body(Map.of("status", "anonymous"));
        }
    }
}
