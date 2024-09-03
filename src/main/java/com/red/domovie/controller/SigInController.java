package com.red.domovie.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.red.domovie.domain.dto.login.FindIdDTO;
import com.red.domovie.domain.dto.login.FindIdResponse;
import com.red.domovie.domain.dto.login.FindPasswordRequestDTO;
import com.red.domovie.domain.dto.login.ResetPasswordRequestDTO;
import com.red.domovie.domain.dto.login.SignUpDTO;
import com.red.domovie.security.CustomUserDetails;
import com.red.domovie.service.LoginService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SigInController {

	private final LoginService loginService;
	private static final Logger logger = LoggerFactory.getLogger(SigInController.class);

	@GetMapping("/signin")
	public String signIn() {
		return "views/login/signin";
	}

	@GetMapping("/findPassword")
	public String findPassword() {
		return "views/login/findPassword";
	}
	@PostMapping("/api/find-password")
    public String findPassword(@RequestBody FindPasswordRequestDTO request) {
        logger.info("비밀번호 찾기 요청 받음: {}", request.getEmail());
        loginService.processFindPassword(request.getUserName(), request.getEmail());
		return "redirect:/findPassword";
    }

    @PutMapping("/api/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        logger.info("비밀번호 재설정 요청 받음");
        loginService.processResetPassword(request.getResetToken(), request.getNewPassword());
        return "redirect:/signin";
    }
	

	@GetMapping("/signup")
	public String signup() {
		return "views/login/signup";
	}
	
	

	@PostMapping("/signup")
	public String signup(@ModelAttribute SignUpDTO signUpDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {
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

	// 이메일 확인
	@PostMapping("/api/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmailDuplication(@RequestBody Map<String, String> payload) {
		String email = payload.get("email");
		Map<String, Boolean> response = loginService.checkEmailDuplication(email);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/findId")
	public String findId() {
		return "views/login/findId";
	}

	@PostMapping("/api/find-id")
	public ResponseEntity<?> findId(@RequestBody FindIdDTO request) {
		String email = loginService.findEmailByNameAndBirthDate(request);
		if (email != null) {
			return ResponseEntity.ok(new FindIdResponse(email));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 로그인 성공 시 리다이렉트될 페이지
	@GetMapping("/")
	public String home() {
		return "/index";
	}

}
