package com.red.domovie.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.red.domovie.domain.dto.login.ErrorResponse;
import com.red.domovie.domain.dto.login.FindIdDTO;
import com.red.domovie.domain.dto.login.FindIdResponse;
import com.red.domovie.domain.dto.login.FindPasswordRequestDTO;
import com.red.domovie.domain.dto.login.PasswordResetRequest;
import com.red.domovie.domain.dto.login.SignUpDTO;
import com.red.domovie.domain.dto.login.SuccessResponse;
import com.red.domovie.service.LoginService;

import jakarta.validation.Valid;
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
	public ResponseEntity<?> findPassword(@Valid @RequestBody FindPasswordRequestDTO request) {
		try {
			//사용자가 넘긴 이메일로 이메일을 발송하기위한 로직
			loginService.processFindPassword(request.getEmail());
			return ResponseEntity.ok().body(new SuccessResponse("비밀번호 재설정 이메일이 발송되었습니다."));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(name="token") String token, Model model) {
        // 토큰 유효성 검사
        loginService.isValidPasswordResetToken(token, model);
        return "views/login/reset-password";
    }

	@PostMapping("/reset-password")
    public String processResetPassword(@RequestParam(name="token") String token,
                                                       @RequestBody PasswordResetRequest request) {
        loginService.resetPassword(token, request.getNewPassword());
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
	    List<String> emails = loginService.findEmailByNameAndBirthDate(request);
	    if (!emails.isEmpty()) {
	        return ResponseEntity.ok(new FindIdResponse(emails));
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	// 로그인 성공 시 리다이렉트될 페이지
	@GetMapping("/")
	public String home() {
		return "index";
	}

}
