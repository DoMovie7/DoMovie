package com.red.domovie.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.security.CustomUserDetails;
import com.red.domovie.service.MypageService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MypageController {

	private final MypageService mypageService;

	// 프로필 페이지이동
	@GetMapping("/mypage")
	public String mypage(Model model) {
	    ProfileDTO user = mypageService.getCurrentUser();
	    if (user != null) {
	        model.addAttribute("user", user);
	    } else {
	        // 사용자 정보가 없을 때의 처리
	        return "redirect:/login";  // 또는 다른 적절한 처리
	    }
	    return "views/user/mypage";
	}
	
	// 프로필 수정 처리
	@PostMapping("/updateProfile")
	@ResponseBody
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ProfileUpdateDTO dto) {
	    try {
	        mypageService.updateProcess(userDetails.getUserId(), dto);
	        return ResponseEntity.ok().body(Map.of("message", "프로필이 성공적으로 업데이트되었습니다."));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	    }
	}
	
	
}
