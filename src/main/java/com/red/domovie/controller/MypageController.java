package com.red.domovie.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.red.domovie.domain.dto.mypage.ProfileDTO;
import com.red.domovie.domain.dto.mypage.ProfileUpdateDTO;
import com.red.domovie.domain.dto.recommend.RecommendListDTO;
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
			return "redirect:/login"; // 또는 다른 적절한 처리
		}
		return "views/user/mypage";
	}

	// 프로필 수정 처리
	@PostMapping("/updateProfile")
	@ResponseBody
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody ProfileUpdateDTO dto) {
		try {
			mypageService.updateProcess(userDetails.getUserId(), dto);
			return ResponseEntity.ok().body(Map.of("message", "프로필이 성공적으로 업데이트되었습니다."));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	@ResponseBody
	@PostMapping("/mypage/profile/temp-upload")
	public Map<String, String> profileTempUpload(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(name = "profile") MultipartFile profile){
		return mypageService.profileImageUpdateProcess(userDetails.getUserId(), profile);
	}
	

	// 내가 쓴 글 불러오기
	@ResponseBody // List<RecommendListDTO> 객체를 JSON 배열로 변환
	@GetMapping("/mypage/recommends")
	public List<RecommendListDTO> recommendsByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 로그인 된 회원 ID를 기반으로 회원이 작성한 추천 글 목록을 조회하여 반환한다.
		return mypageService.recommendsByUserProcess(userDetails.getUserId());
	}

}
