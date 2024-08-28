package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MovieDetailController {
	
	@GetMapping("/movie/detail")
	public String getMethodName() {
		return "";
	}
	

}
