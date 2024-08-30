package com.red.domovie.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.red.domovie.domain.dto.SignUpDTO;
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
	@PostMapping("/signup")
    public String signup(@ModelAttribute SignUpDTO signUpDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            loginService.saveProcess(signUpDTO);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/signin";
        } catch (IllegalArgumentException e) {
            result.rejectValue("", "error.signUpDTO", e.getMessage());
        } catch (DuplicateKeyException e) {
            result.rejectValue("email", "error.email", "이미 사용 중인 이메일입니다.");
        } catch (Exception e) {
            result.rejectValue("", "error.signUpDTO", "회원가입 중 오류가 발생했습니다.");
        }
        return "views/login/signup";
    }
	
	
	// 로그인 성공 시 리다이렉트될 페이지
    @GetMapping("/")
    public String home() {
        return "/index";
    }
    
	
}
