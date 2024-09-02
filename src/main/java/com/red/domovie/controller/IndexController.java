package com.red.domovie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.red.domovie.service.MovieApiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final MovieApiService movieApiService;
    
    //비동기로 요청하면 html이 응답데이터
    @GetMapping("/movies/new")
    public String getNewMovies(Model model) {
        // 영화 정보 가져오기
        movieApiService.getNewMovies(model);
        
        return "views/index-movie-list";  // 타임리프 템플릿 이름 반환
    }
    
    
    @GetMapping("/movies/boxOffice")
    public String getBoxOffice(Model model) {
        // 모든 영화 정보를 서비스로부터 받아옴
    	movieApiService.getBoxOffice(model);
        return "views/index-box-movie-list";
    }
    
    
}
