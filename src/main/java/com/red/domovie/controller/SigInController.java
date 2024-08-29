package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SigInController {
	
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
}
