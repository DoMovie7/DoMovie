package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.red.domovie.service.LoginService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequiredArgsConstructor
public class SigInController {
	
	private final LoginService loginService;
	
	@GetMapping("/signin")
	public String signIn() {
		return "views/login/signin";
	}
	@GetMapping("/findId")
	public String findId() {
		return "views/login/findId";
	}
	@GetMapping("/findPassword")
	public String findPassword() {
		return "views/login/findPassword";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "views/login/signup";
	}
	
	// 로그인 성공 시 리다이렉트될 페이지
    @GetMapping("/")
    public String home() {
        return "/index";
    }
	
}
