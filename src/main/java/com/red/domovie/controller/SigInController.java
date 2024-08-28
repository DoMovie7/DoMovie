package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SigInController {
	
	@GetMapping("/signin")
	public String getMethodName() {
		return "views/login/signin";
	}
	
}
