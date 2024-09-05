package com.red.domovie.controller;

import org.springframework.context.annotation.Role;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.red.domovie.domain.dto.recommend.RecommendSaveDTO;
import com.red.domovie.domain.entity.RecommendEntity;
import com.red.domovie.security.CustomUserDetails;
import com.red.domovie.service.RecommendService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



// 이 클래스는 Spring MVC의 컨트롤러 역할을 수행합니다.
@Controller
@RequiredArgsConstructor // 생성자 주입을 위해 Lombok의 @RequiredArgsConstructor를 사용합니다.
public class RecommendController {

    // 의존성 주입을 위해 RecommendService를 주입받습니다.
    private final RecommendService recommendService;

    // 기본 생성자는 Lombok의 @RequiredArgsConstructor가 자동으로 생성합니다.
    // @Autowired를 사용하여 의존성 주입을 수행할 수 있지만, @RequiredArgsConstructor를 사용하고 있습니다.
    // @Autowired
    // private final RecommendService recommendService;

    /*
    // @Autowired 어노테이션을 사용한 생성자 주입의 예시입니다.
    @Autowired
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }
    //*/
    // 새로운 추천 글 작성 페이지를 보여주는 메서드입니다.
    @GetMapping("/recommends/new")
    public String newRecommend() {
        return "views/recommend/write"; // "write" 뷰를 반환하여 글쓰기 페이지로 이동합니다.
    }

    // GET 요청에 대해 "/recommends" 경로를 처리합니다.
    @GetMapping("/recommends")
    public String list(Model model) {
        // recommendService의 listProcess 메서드를 호출하여 모델을 설정합니다.
        recommendService.listProcess(model);
        // "views/recommend/list"라는 뷰를 반환합니다. 이 뷰는 Thymeleaf 템플릿을 참조합니다.
        return "views/recommend/list";
    }
    
	/*
	 * spring-security 인증 참조사이트
	 * https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontext
	 */    // 새로운 추천 글을 저장하는 메서드입니다.
    @PostMapping("/recommends") //작성자 정보는 session()에 저장 어센티케이션-프린시펄
    public String createRecommend(@ModelAttribute RecommendSaveDTO dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    	recommendService.savePost(dto, customUserDetails.getUserId()); // 입력된 추천 글 데이터를 데이터베이스에 저장합니다.
        return "redirect:/recommends"; // 저장 후 추천 목록 페이지로 리다이렉트합니다.
    }
    
    @GetMapping("/recommends/{id}")
    public String viewRecommend(@PathVariable("id") Long id, Model model) {
        RecommendEntity recommendEntity = recommendService.getPost(id);
        model.addAttribute("recommend", recommendEntity);
        return "views/recommend/detail"; // 상세 페이지를 반환합니다.
    }
    
    @GetMapping("/genres/{genreIdx}/recommendations")
    public String listByGenre(Model model, @PathVariable("genreIdx") int genreIdx) {
        // recommendService의 listProcess 메서드를 호출하여 모델을 설정합니다.
        recommendService.listProcess(model);
        
        // "views/recommend/list"라는 뷰를 반환합니다. 이 뷰는 Thymeleaf 템플릿을 참조합니다.
        return "views/recommend/list";
    }
    
    
    
   
    
}
