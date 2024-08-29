package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class MypageController {

	@GetMapping("/mypage")
	public String list() {
		return "/views/user/mypage";
	}
	
	
	
}
