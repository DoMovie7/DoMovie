package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SignInController {
	
	@GetMapping("/")
	public String list() {
		return "views/login/signin";
	}
	
}
