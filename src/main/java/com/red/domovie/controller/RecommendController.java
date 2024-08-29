package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.red.domovie.service.RecommendService;

import lombok.RequiredArgsConstructor;

// 이 클래스는 Spring MVC의 컨트롤러 역할을 수행합니다.
@Controller
@RequestMapping("/recommends") // "/recommends" 경로에 대한 요청을 처리합니다.
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

    // GET 요청에 대해 "/recommends" 경로를 처리합니다.
    @GetMapping
    public String list(Model model) {
        // recommendService의 listProcess 메서드를 호출하여 모델을 설정합니다.
        recommendService.listProcess(model);
        
        // "views/recommend/list"라는 뷰를 반환합니다. 이 뷰는 Thymeleaf 템플릿을 참조합니다.
        return "views/recommend/list";
    }
}
